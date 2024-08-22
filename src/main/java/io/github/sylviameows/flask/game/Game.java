package io.github.sylviameows.flask.game;

import io.github.sylviameows.flask.registries.GameRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public abstract class Game {
    private final Plugin plugin;
    private Settings settings;
    private final Queue<?> queue;

    private NamespacedKey key;

    private Integer totalPlayers;

    protected Game(Plugin plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;

        this.queue = new Queue<>(this);
    }

    public boolean register(String key) {
        if (this.key != null) return false;
        this.key = new NamespacedKey(plugin, key);
        GameRegistry.instance().add(this);
        return true;
    }

    abstract public void createLobby();
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
    public Plugin getPlugin() {
        return plugin;
    }
}
