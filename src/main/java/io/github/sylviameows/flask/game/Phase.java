package io.github.sylviameows.flask.game;

import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

public interface Phase extends Listener {
    /* TODO */

    void onEnabled(Lobby<?> parent);
    void onDisabled();

    void onPlayerJoin(Player player);
    void onPlayerLeave(Player player);

    Phase next();
}
