package io.github.sylviameows.flask.managers;

import io.github.sylviameows.flask.api.manager.Manager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

abstract class ManagerImpl<T> implements Manager<T> {
    protected final Map<String, T> map;

    protected ManagerImpl() {
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