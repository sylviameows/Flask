package io.github.sylviameows.flask.listeners;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.managers.PlayerManagerImpl;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class JoinListener implements Listener {
    public static void register(Flask plugin) {
        Bukkit.getPluginManager().registerEvents(new JoinListener(), plugin);
    }

    @EventHandler
    private void join(PlayerJoinEvent event) {
        PlayerManagerImpl.instance().add(event.getPlayer());
    }
}
