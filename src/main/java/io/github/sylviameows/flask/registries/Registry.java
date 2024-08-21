package io.github.sylviameows.flask.registries;

import org.bukkit.NamespacedKey;
import org.bukkit.plugin.Plugin;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class Registry<T> {
    private final Map<NamespacedKey, T> map;

    private Registry() {
        this.map = new ConcurrentHashMap<>();
    }

    public T add(NamespacedKey key, T entry) {
        return map.put(key, entry);
    }
    public T add(Plugin plugin, String key, T entry) {
        return add(new NamespacedKey(plugin, key), entry);
    }

    public T get(NamespacedKey key) {
        return map.get(key);
    }
    public T get(Plugin plugin, String key) {
        return get(new NamespacedKey(plugin, key));
    }

    public T remove(NamespacedKey key) {
        return map.remove(key);
    }
    public T remove(Plugin plugin, String key) {
        return remove(new NamespacedKey(plugin, key));
    }
}