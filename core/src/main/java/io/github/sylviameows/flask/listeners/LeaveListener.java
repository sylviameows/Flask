package io.github.sylviameows.flask.listeners;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.managers.PlayerManagerImpl;
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
        var flask = PlayerManagerImpl.instance().get(player);
        var game = flask.getGame();
        if (game != null) {
            game.getQueue().removePlayer(player);
        }

        var lobby = flask.getLobby();
        if (lobby != null) {
            lobby.removePlayer(player);
        }

        // todo get game and lobby and fire disconnect event.
    }
}
