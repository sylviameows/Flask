package io.github.sylviameows.flask.api.game.map;

import io.github.sylviameows.flask.api.annotations.MapProperty;
import org.bukkit.Location;

public class FlaskMap implements GameMap {
    private final String id;

    @MapProperty(name = "Lobby Spawn", description = "The location a player will spawn while waiting for the game to start.")
    Location waiting;

    @MapProperty(name = "Allow Spectators", description = "When enabled, players will be allowed to spectate on this map.")
    boolean spectators = true;

    public FlaskMap(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }
}
