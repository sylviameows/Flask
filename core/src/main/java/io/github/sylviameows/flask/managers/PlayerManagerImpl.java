package io.github.sylviameows.flask.managers;

import io.github.sylviameows.flask.api.FlaskPlayer;
import io.github.sylviameows.flask.api.manager.PlayerManager;
import io.github.sylviameows.flask.players.FlaskPlayerImpl;
import org.bukkit.entity.Player;

/**
 * Links Bukkit player instances to a {@link FlaskPlayerImpl} object for ease of tracking.
 */
public class PlayerManagerImpl extends ManagerImpl<FlaskPlayer> implements PlayerManager {
    private static final PlayerManager instance = new PlayerManagerImpl();
    public static PlayerManager instance() {
        return instance;
    }

    public FlaskPlayer add(Player player) {
        return map.put(player.getUniqueId().toString(), new FlaskPlayerImpl(player));
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
