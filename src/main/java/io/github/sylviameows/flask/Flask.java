package io.github.sylviameows.flask;

import io.github.sylviameows.flask.examples.ExampleGame;
import io.github.sylviameows.flask.hub.holograms.GameHologram;
import io.github.sylviameows.flask.registries.GameRegistry;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * The main plugin file, where access to all necessary information is given.
 */
public class Flask extends JavaPlugin {
    public static ComponentLogger logger;

    @Override
    public void onEnable() {
        logger = getComponentLogger();

        var games = GameRegistry.instance();
        new ExampleGame(this).register("example");
        logger.info("Registered game: "+games.get(this, "example").settings.getName());

        var location = new Location(Bukkit.getWorld("world"), 7.5, -59.0, -3.5);
        new GameHologram(games.get(this, "example"), location);

        // display plugin loaded message (aka motd)
        for (Component component : motd()) {
            logger.info(component);
        }

        // registry testing



    }

    @Override
    public void onDisable() {

        logger.info("Purging flask entities...");
        var worlds = Bukkit.getWorlds();
        for (World world : worlds) {
            var purge = world.getEntities().stream().filter(entity ->
                    entity.getPersistentDataContainer().has(new NamespacedKey("flask", "purge"))
            ).toList();
            var count = purge.size();
            if (count > 0) {
                purge.forEach(Entity::remove);
                logger.info("Purged "+count+" flask entities from "+world.getName());
            }
        }
        logger.info("Flask entities purged!");
    }

    /**
     * Gets the plugins message of the day ("motd"), using a random message.
     * @return a list of each component in the motd.
     */
    private Component @NotNull [] motd() {
        List<String> messages = Arrays.asList(
                "shipping untested code to your server's doorstep.",
                "preparing for a science experiment gone wrong.",
                "who even reads these messages anyway?",
                "powering minigames using \"chemistry\" since 2024.",
                "<- is this one of those mark rober volcanos?",
                "so uh.. what the hell is a vial and why do i open it?"
        );
        var random = new Random();
        String message = messages.get(random.nextInt(messages.size()));

        return new Component[]{
                Component.text("  ] [").color(Palette.MINT),
                Component.text("  |~|   Flask v"+this.getPluginMeta().getVersion()+" loaded successfully.").color(Palette.MINT),
                Component.text(" /o  \\   ").color(Palette.MINT).append(Component.text(message).color(Palette.WHITE)),
                Component.text("/___o_\\").style(Style.style(Palette.MINT, TextDecoration.UNDERLINED))
        };
    }
}