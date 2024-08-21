package io.github.sylviameows.flask.listeners;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.managers.PlayerManager;
import io.github.sylviameows.flask.players.FlaskPlayer;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class LeaveListener implements Listener {
    public static void register(Flask plugin) {
        Bukkit.getPluginManager().registerEvents(new LeaveListener(), plugin);
    }

    @EventHandler
    private void quit(PlayerQuitEvent event) {
        var flaskPlayer = PlayerManager.instance().remove(event.getPlayer());
        handleDisconnect(flaskPlayer);
    }

    @EventHandler
    private void quit(PlayerKickEvent event) {
        var flaskPlayer = PlayerManager.instance().remove(event.getPlayer());
        handleDisconnect(flaskPlayer);
    }

    private void handleDisconnect(FlaskPlayer player) {
        // todo get game and lobby and fire disconnect event.
    }
}
