package io.github.sylviameows.flask.api.registry;

import io.github.sylviameows.flask.api.game.Game;
import org.bukkit.NamespacedKey;

import java.util.List;

public interface GameRegistry extends Registry<Game> {
    Game add(Game game);
    Game remove(Game game);

    List<NamespacedKey> keys();
    Game findByName(String name);
}
