package io.github.sylviameows.flask.hub.holograms;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.api.game.Settings;
import io.github.sylviameows.flask.hub.holograms.tasks.GameHologramManagerTask;
import io.github.sylviameows.flask.managers.HologramManager;
import io.github.sylviameows.flask.registries.GameRegistryImpl;
import org.apache.commons.lang.NullArgumentException;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.bukkit.util.Vector;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class GameHologram {
    private final Game game;
    private final Location location;

    private final UUID uuid;

    private final ItemDisplay display;
    private final Interaction interaction;

    private final GameHologramManagerTask task;

    public static void load() {
        var logger = Flask.logger;
        logger.info("Finding holograms...");
        List<Interaction> selected = new ArrayList<>();

        var worlds = Bukkit.getWorlds();
        for (World world : worlds) {
            var interactions = world.getEntitiesByClass(Interaction.class);
            for (Interaction interaction : interactions) {
                var data = interaction.getPersistentDataContainer().get(new NamespacedKey("flask", "special"), PersistentDataType.STRING);
                if (Objects.equals(data, "hologram_interaction")) selected.add(interaction);
            }
        }
        logger.info("Found "+selected.size()+" hologram(s) in "+worlds.size()+" world(s), starting initialization...");
        int failures = 0;
        for (Interaction interaction : selected) {
            try {
                new GameHologram(interaction);
            } catch (Exception e) {
                failures++;
            }
        }
        if (failures > 0) logger.warn("Failed to initialize "+failures+" hologram(s)!");
        logger.info("Initialized "+(selected.size()-failures)+" hologram(s) successfully.");
    }

    public GameHologram(Game game, Location location) throws IllegalArgumentException {
//        if (game.getKey() == null) throw new IllegalArgumentException("Provided game is not registered!");
        this.game = game;
        this.location = location.toCenterLocation().setDirection(new Vector(1,0,0));

        display = spawnDisplay();
        interaction = spawnInteraction();

        uuid = interaction.getUniqueId(); // for calling back to this task

        // link the two entities together, so if you find one you can find the other.
        linkEntities(display, interaction);

        task = new GameHologramManagerTask(game, display);
        task.runTaskTimer(game.getPlugin(), 0L, 1L);

        HologramManager.instance().add(uuid.toString(), this);
    }

    public GameHologram(Interaction interaction) throws NullArgumentException, IllegalArgumentException {
        this.interaction = interaction;
        var companion = findCompanion(interaction);
        if (companion instanceof ItemDisplay disp) {
            this.display = disp;

            var gameKey = interaction.getPersistentDataContainer().get(new NamespacedKey("flask", "game"), PersistentDataType.STRING);
            if (gameKey == null) throw new NullArgumentException("Game value on interaction entity is null!");
            this.game = GameRegistryImpl.instance().get(NamespacedKey.fromString(gameKey));
            this.location = display.getLocation();

            this.uuid = interaction.getUniqueId();

            if (game != null) {
                this.task = new GameHologramManagerTask(game, display);
            } else {
                this.task = new GameHologramManagerTask(NamespacedKey.fromString(gameKey), display);
            }

            task.runTaskTimer(game.getPlugin(), 0L, 1L);

            HologramManager.instance().add(uuid.toString(), this);
        } else {
            throw new IllegalArgumentException("Companion was not an item display!");
        }
    }

    private Entity findCompanion(Entity entity) {
        String uuidString = interaction.getPersistentDataContainer().get(new NamespacedKey("flask", "companion"), PersistentDataType.STRING);
        if (uuidString == null) throw new NullArgumentException("Could not find companion data!");
        Entity companion = Bukkit.getEntity(UUID.fromString(uuidString));
        if (companion == null) throw new NullArgumentException("Could not find companion entity!");
        return companion;
    }

    public void remove() {
        display.remove();
        interaction.remove();
        task.getHolograms().forEach(Entity::remove);
        task.cancel();
    }

    private void linkEntities(Entity a, Entity b) {
        a.getPersistentDataContainer().set(
                new NamespacedKey("flask", "companion"),
                PersistentDataType.STRING,
                b.getUniqueId().toString()
        );
        b.getPersistentDataContainer().set(
                new NamespacedKey("flask", "companion"),
                PersistentDataType.STRING,
                a.getUniqueId().toString()
        );
    }

    private ItemDisplay spawnDisplay() {
        Settings settings = game.getSettings();
        Material icon = settings.getIcon();

        ItemDisplay entity = (ItemDisplay) location.getWorld().spawnEntity(location, EntityType.ITEM_DISPLAY);
        ItemStack itemStack = new ItemStack(icon, 1);

        // display entity settings
        entity.setBillboard(Display.Billboard.FIXED);
        entity.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GUI);
        entity.setItemStack(itemStack);
        entity.setTeleportDuration(2);
        entity.setTransformation(new Transformation(
                new Vector3f(0,0,0),
                new Quaternionf(),
                new Vector3f(0.9f, 0.9f, 0.9f),
                new Quaternionf()
        ));

        // entity information
        var pdc = entity.getPersistentDataContainer();
        pdc.set(new NamespacedKey("flask","special"), PersistentDataType.STRING, "hologram_display");


        return entity;
    }

    private Interaction spawnInteraction() {
        Location interactionLocation = location.clone();
        interactionLocation.setY(interactionLocation.y() - 0.45);

        Interaction entity = (Interaction) interactionLocation.getWorld().spawnEntity(interactionLocation, EntityType.INTERACTION);

        // interaction entity settings
        entity.setInteractionWidth(0.9f);
        entity.setInteractionHeight(0.9f);

        // entity information
        var pdc = entity.getPersistentDataContainer();
        pdc.set(new NamespacedKey("flask","special"), PersistentDataType.STRING, "hologram_interaction");
        pdc.set(new NamespacedKey("flask","game"), PersistentDataType.STRING, game.getKey().asString());

        return entity;
    }
}
