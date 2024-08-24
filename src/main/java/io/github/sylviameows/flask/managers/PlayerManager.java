package io.github.sylviameows.flask.managers;

import io.github.sylviameows.flask.players.FlaskPlayer;
import org.bukkit.entity.Player;

/**
 * Links Bukkit player instances to a {@link FlaskPlayer} object for ease of tracking.
 */
public class PlayerManager extends Manager<FlaskPlayer> {
    private static final PlayerManager instance = new PlayerManager();
    public static PlayerManager instance() {
        return instance;
    }

    public FlaskPlayer add(Player player) {
        return map.put(player.getUniqueId().toString(), new FlaskPlayer(player));
    }

    public boolean has(Player player) {
        return map.containsKey(player.getUniqueId().toString());
    }

    public FlaskPlayer get(Player player) {
        return map.get(player.getUniqueId().toString());
    }

    public FlaskPlayer remove(Player player) {
        return map.remove(player.getUniqueId().toString());
    }
}
