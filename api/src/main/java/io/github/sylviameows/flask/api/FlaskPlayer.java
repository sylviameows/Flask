package io.github.sylviameows.flask.api;

import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.api.game.Lobby;
import io.github.sylviameows.flask.api.game.map.GameMap;

public interface FlaskPlayer {
    void setGame(Game<? extends GameMap> game);

    void setLobby(Lobby<?> lobby);

    Game<? extends GameMap> getGame();

    Lobby<?> getLobby();

    void reset();

    boolean isOccupied();

    boolean isInQueue();
}
