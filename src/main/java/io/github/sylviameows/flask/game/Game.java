package io.github.sylviameows.flask.game;

import org.bukkit.plugin.Plugin;

public abstract class Game {
    private Plugin plugin;
    public Settings settings;

    protected Game(Plugin plugin, Settings settings) {
        this.plugin = plugin;
        this.settings = settings;
    }

    abstract public void createLobby();
    abstract public Phase initialPhase();

    public Plugin getPlugin() {
        return plugin;
    }
}
