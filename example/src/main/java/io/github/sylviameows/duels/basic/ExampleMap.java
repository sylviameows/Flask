package io.github.sylviameows.duels.basic;

import io.github.sylviameows.flask.api.game.map.FlaskMap;
import io.github.sylviameows.flask.api.annotations.MapProperty;
import org.bukkit.Location;

public class ExampleMap extends FlaskMap {
    @MapProperty
    Location spawn_a;

    @MapProperty
    Location spawn_b;

    public ExampleMap(String id) {
        super(id);
    }
}
