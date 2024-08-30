package io.github.sylviameows.flask.api;

import org.bukkit.plugin.java.JavaPlugin;

public abstract class FlaskPlugin extends JavaPlugin {
    abstract public FlaskAPI getFlaskAPI();
}
