package io.github.sylviameows.flask.hub.holograms;

import io.github.sylviameows.flask.game.Game;
import io.github.sylviameows.flask.game.Settings;
import io.github.sylviameows.flask.hub.holograms.tasks.GameHologramManagerTask;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.UUID;

public class GameHologram {
    private final Game game;
    private final Location location;

    private final UUID uuid; // TODO append to attached entities somehow?

    private final ItemDisplay display;
    private final Interaction interaction;

    private final GameHologramManagerTask task;

    // TODO: generate from item display maybe?
    public GameHologram(Game game, Location location) throws IllegalArgumentException {
        if (game.getKey() == null) throw new IllegalArgumentException("Provided game is not registered!");
        this.game = game;
        this.location = location;
        this.uuid = UUID.randomUUID();

        display = spawnDisplay();
        interaction = spawnInteraction();

        task = new GameHologramManagerTask(game, display);
        task.runTaskTimer(game.getPlugin(), 0L, 1L);
    }

    public void remove() {
        display.remove();
        interaction.remove();
        task.getHolograms().forEach(Entity::remove);
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
