package io.github.sylviameows.flask.hub.holograms.tasks;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.api.Palette;
import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.api.game.Settings;
import io.github.sylviameows.flask.registries.GameRegistryImpl;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.*;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Transformation;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public final class GameHologramManagerTask extends BukkitRunnable {
    private Game game;
    private final ItemDisplay display;
    private final Map<String, TextDisplay> holograms = new HashMap<>();

    private final NamespacedKey key;
    private int loops = 0;

    public GameHologramManagerTask(Game game, ItemDisplay display) {
        this.game = game;
        this.display = display;

        this.key = game.getKey();
    }

    /**
     * registers as an unregistered hologram.
     * @param key
     * @param display
     */
    public GameHologramManagerTask(NamespacedKey key, ItemDisplay display) {
        this.game = null;
        this.display = display;

        this.key = key;
    }

    @Override
    public void run() {
        if (this.game == null) {
            // tries to find the host game if it doesn't exist, otherwise holograms break.
            // will only run every 20 ticks.
            // possibly make this stop trying after a while?
            loops++;

            if (loops >= 20) loops = 0;
            if (loops == 0) {
                this.game = GameRegistryImpl.instance().get(key);
            }
        }

        // spin item display
        Location location = display.getLocation();
        var yaw = location.getYaw() + 2;
        if (yaw >= 180) yaw = yaw - 360;

        location.setYaw(yaw);
        display.teleport(location);

        // get nearby players
        var players = location.getNearbyPlayers(2.8);
        if (players.isEmpty() && holograms.isEmpty()) return;

        // add new players without holograms
        for (Player player : players) {
            if (holograms.containsKey(player.getName())) continue;

            TextDisplay hologram = spawnText();
            hologram.setVisibleByDefault(false);

            new GameHologramShowTask(hologram, 20, Flask.getInstance()).runTask(Flask.getInstance());

            player.showEntity(Flask.getInstance(), hologram);
            holograms.put(player.getName(), hologram);
        }

        // remove players too far from hologram
        var nearbyPlayers = players.stream().map(Player::getName).toList();
        var hologramViewers = holograms.keySet().stream().toList(); // fix concurrency error hopefully
        for (String name : hologramViewers) {
            if (nearbyPlayers.contains(name)) continue;

            TextDisplay hologram = holograms.get(name);
            new GameHologramHideTask(hologram, 15, Flask.getInstance()).runTask(Flask.getInstance());
            holograms.remove(name);
        }

        // update remaining holograms texts
        Component hologramText = getHologramComponent();
        for (TextDisplay hologram : holograms.values()) {
            hologram.text(hologramText);
        }
    }

    private TextDisplay spawnText() {
        Location location = display.getLocation();
        TextDisplay hologram = (TextDisplay) display.getWorld().spawnEntity(location, EntityType.TEXT_DISPLAY);

        // text display settings
        hologram.setBillboard(Display.Billboard.VERTICAL);
        hologram.setAlignment(TextDisplay.TextAlignment.LEFT);
        hologram.setLineWidth(150);
        hologram.setTransformation(new Transformation(
                new Vector3f(1.4f, -0.3f, 0),
                new Quaternionf(),
                new Vector3f(0.5f, 0.5f, 0.5f),
                new Quaternionf()
        ));
        hologram.setBackgroundColor(Color.fromARGB(0x70000000));
        hologram.setShadowed(true);

        // entity settings
        var pdc = hologram.getPersistentDataContainer();
        pdc.set(new NamespacedKey("flask", "purge"), PersistentDataType.BOOLEAN, true);

        return hologram;
    }

    private Component getHologramComponent() {
        if (game == null) {
            return Component.text("could not find game: "+key.asString()).color(io.github.sylviameows.flask.api.Palette.RED_LIGHT);
        }


        Settings settings = game.getSettings();
        return Component.text(settings.getName()).color(settings.getColor())
                .append(Component.text(" • "+game.getQueue().getTotalPlayers()+" players").color(Palette.WHITE))
                .appendNewline()
                .append(Component.text("("+game.getQueue().getQueueSize()+" in queue)").color(Palette.GRAY))
                .appendNewline().appendNewline()
                .append(MiniMessage.miniMessage().deserialize(settings.getDescription()).colorIfAbsent(Palette.WHITE))
                .appendNewline().appendNewline()
                .append(Component.text("⏵ ").color(settings.getColor()))
                .append(Component.text("Join Game").color(Palette.WHITE))
                .append(Component.text(" (right click)").color(Palette.GRAY));
    }

    public Collection<TextDisplay> getHolograms() {
        return holograms.values();
    }
}
