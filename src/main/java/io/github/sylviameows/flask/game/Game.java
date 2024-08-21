package io.github.sylviameows.flask.game;

import io.github.sylviameows.flask.registries.GameRegistry;
import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public abstract class Game {
    private final Plugin plugin;
    public Settings settings;

    private NamespacedKey key;

    protected Game(Plugin plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
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
    public NamespacedKey getKey() {
        return key;
    }
    public Plugin getPlugin() {
        return plugin;
    }
}
