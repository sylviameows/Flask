package io.github.sylviameows.flask.api.manager;

import io.github.sylviameows.flask.api.FlaskPlayer;
import org.bukkit.entity.Player;

/**
 * Links Bukkit player instances to a {@link FlaskPlayer} object for ease of tracking.
 */
public interface PlayerManager extends Manager<FlaskPlayer> {
    FlaskPlayer add(Player player);
    boolean has(Player player);
    FlaskPlayer get(Player player);
    FlaskPlayer remove(Player player);
}
