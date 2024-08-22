package io.github.sylviameows.flask.listeners;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.managers.PlayerManager;
import io.github.sylviameows.flask.players.FlaskPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
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
        handleDisconnect(event.getPlayer());
    }

    @EventHandler
    private void quit(PlayerKickEvent event) {
        handleDisconnect(event.getPlayer());
    }

    private void handleDisconnect(Player player) {
        var flask = PlayerManager.instance().get(player);
        var game = flask.getGame();
        if (game != null) {
            game.getQueue().leave(player);
        }

        var lobby = flask.getLobby();

        // todo get game and lobby and fire disconnect event.
    }
}