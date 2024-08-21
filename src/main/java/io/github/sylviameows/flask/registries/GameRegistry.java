package io.github.sylviameows.flask.registries;

import io.github.sylviameows.flask.game.Game;

public class GameRegistry extends Registry<Game> {
    private static final GameRegistry instance = new GameRegistry();
    public static GameRegistry instance() {
        return instance;
    }
}
