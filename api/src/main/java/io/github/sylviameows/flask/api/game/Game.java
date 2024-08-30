package io.github.sylviameows.flask.api.game;

import io.github.sylviameows.flask.api.FlaskPlugin;
import io.github.sylviameows.flask.api.registry.GameRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.List;

public abstract class Game {
    private final FlaskPlugin plugin;
    private Settings settings;
    private final Queue<?> queue;

    private NamespacedKey key;

    private Integer totalPlayers;

    protected Game(FlaskPlugin plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;

        this.queue = new Queue<>(this);
    }

    public boolean register(String key) {
        if (this.key != null) return false;
        this.key = new NamespacedKey(plugin, key);
        plugin.getFlaskAPI().getGameRegistry().add(this);
        return true;
    }

    abstract public Lobby<?> createLobby(List<Player> players);
    abstract public Phase initialPhase();

    public Settings getSettings() {
        return settings;
    }
    public Queue<?> getQueue() {
        return queue; 
    }
    public NamespacedKey getKey() {
        return key;
    }
    public FlaskPlugin getPlugin() {
        return plugin;
    }
}