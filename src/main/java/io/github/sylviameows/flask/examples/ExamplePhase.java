package io.github.sylviameows.flask.examples;

import io.github.sylviameows.flask.game.Lobby;
import io.github.sylviameows.flask.game.Phase;

public class ExamplePhase implements Phase {
    @Override
    public void onEnabled(Lobby<?> parent) {

    }

    @Override
    public void onDisabled() {

    }

    @Override
    public Phase next() {
        return new ExamplePhase();
    }
}
