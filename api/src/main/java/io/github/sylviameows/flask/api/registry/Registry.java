package io.github.sylviameows.flask.api.registry;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

public interface Registry<T> {
    T add(NamespacedKey key, T entry);
    T add(Plugin plugin, String key, T entry);

    T get(NamespacedKey key);
    T get(Plugin plugin, String key);

    T remove(NamespacedKey key);
    T remove(Plugin plugin, String key);
}
