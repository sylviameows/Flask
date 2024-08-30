package io.github.sylviameows.flask.managers;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class Manager<T> {
    protected final Map<String, T> map;

    protected Manager() {
        this.map = new ConcurrentHashMap<>();
    }

    public T add(String key, T entry) {
        return map.put(key, entry);
    }

    public T get(String key) {
        return map.get(key);
    }

    public T remove(String key) {
        return map.remove(key);
    }
}