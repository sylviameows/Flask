package io.github.sylviameows.flask.api;

import io.github.sylviameows.flask.api.manager.PlayerManager;
import io.github.sylviameows.flask.api.registry.GameRegistry;

public interface FlaskAPI {
    GameRegistry getGameRegistry();
    PlayerManager getPlayerManager();
}