package io.github.sylviameows.flask.registries;

import io.github.sylviameows.flask.game.Game;
import org.bukkit.NamespacedKey;

import java.util.ArrayList;
import java.util.List;

/**
 * Used to manage and load games from multiple plugin sources.
 */
public class GameRegistry extends Registry<Game> {
    private static final GameRegistry instance = new GameRegistry();
    public static GameRegistry instance() {
        return instance;
    }

    public Game add(Game game) {
        if (game.getKey() == null) return null;
        return map.put(game.getKey(), game);
    }
    public Game remove(Game game) {
        if (game.getKey() == null) return null;
        return map.remove(game.getKey());
    }

    public List<NamespacedKey> keys() {
        return new ArrayList<>(map.keySet());
    }
    public Game findByName(String name) {
        for (NamespacedKey key : map.keySet()) {
            if (key.value().equals(name)) return get(key);
        }
        return null;
    }
}
