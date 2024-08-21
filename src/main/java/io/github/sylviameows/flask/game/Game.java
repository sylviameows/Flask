package io.github.sylviameows.flask.game;

abstract class Game {
    public Settings settings;

    protected Game(Settings settings) {
        this.settings = settings;
    }

    abstract public void createLobby();
}
