package io.github.sylviameows.flask.game;

import org.bukkit.event.Listener;

public interface Phase extends Listener {
    /* TODO */

    void onEnabled(Lobby<?> parent);
    void onDisabled();

    Phase next();
}
