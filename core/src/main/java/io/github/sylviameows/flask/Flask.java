package io.github.sylviameows.flask;

import io.github.sylviameows.flask.api.FlaskAPI;
import io.github.sylviameows.flask.api.FlaskPlugin;
import io.github.sylviameows.flask.api.manager.PlayerManager;
import io.github.sylviameows.flask.api.registry.GameRegistry;
import io.github.sylviameows.flask.commands.hologram.HologramCommand;
import io.github.sylviameows.flask.commands.queue.QueueCommand;
import io.github.sylviameows.flask.examples.ExampleGame;
import io.github.sylviameows.flask.hub.holograms.GameHologram;
import io.github.sylviameows.flask.listeners.JoinListener;
import io.github.sylviameows.flask.listeners.LeaveListener;
import io.github.sylviameows.flask.listeners.RightClickEntity;
import io.github.sylviameows.flask.managers.PlayerManagerImpl;
import io.github.sylviameows.flask.registries.GameRegistryImpl;
import io.github.sylviameows.flask.services.MessageService;
import io.github.sylviameows.flask.services.world.FileWorldService;
import io.github.sylviameows.flask.services.world.WorldService;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;
import net.kyori.adventure.text.logger.slf4j.ComponentLogger;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * The main plugin file, where access to all necessary information is given.
 */
public class Flask extends FlaskPlugin implements FlaskAPI {
    public static ComponentLogger logger;
    private static MessageService messageService;
    private static WorldService worldService;
    private static Flask instance;

    @Override
    public void onEnable() {
        instance = this;
        logger = getComponentLogger();

        var games = GameRegistryImpl.instance();
        new ExampleGame(this).register("example");
        logger.info("Registered game: "+games.get(this, "example").getSettings().getName());

        GameHologram.load();

        RightClickEntity.register(this);
        JoinListener.register(this);
        LeaveListener.register(this);

        Flask.messageService = new MessageService(this);
        Flask.worldService = new FileWorldService();

        // commands
        registerCommands();

        // display plugin loaded message (aka motd)
        for (Component component : motd()) {
            logger.info(component);
        }
    }

    @Override
    public void onDisable() {
        purgeFlaskEntities();
    }


    public void registerCommands() {
        var lifecycle = this.getLifecycleManager();
        lifecycle.registerEventHandler(LifecycleEvents.COMMANDS, event -> {
            final Commands commands = event.registrar();
            commands.register(new QueueCommand().build(), List.of("q")); // todo: figure out why this works but not the other way
            new HologramCommand().register(commands);
        });
    }

    public static MessageService getMessageService() {
        return messageService;
    }
    public static WorldService getWorldService() {
        return worldService;
    }
    public static Flask getInstance() { // todo(): try to remove usages
        return instance;
    }

    /**
     * Removes any entities with the pdc tag "flask:purge," use to remove any potentially hanging text displays on shutdown.
     */
    private void purgeFlaskEntities() {
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
     * @return an array of each component in the motd.
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

    @Override
    public GameRegistry getGameRegistry() {
        return GameRegistryImpl.instance();
    }

    @Override
    public PlayerManager getPlayerManager() {
        return PlayerManagerImpl.instance();
    }

    @Override
    public FlaskAPI getFlaskAPI() {
        return this;
    }
}