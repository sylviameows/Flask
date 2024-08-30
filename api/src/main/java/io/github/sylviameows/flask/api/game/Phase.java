package io.github.sylviameows.flask.api.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface Phase extends Listener {
    /* TODO */

    void onEnabled(Lobby<?> parent);
    void onDisabled();

    default void onPlayerJoin(Player player) {}
    default void onPlayerLeave(Player player) {}

    Phase next();
}
