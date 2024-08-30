package io.github.sylviameows.flask.api.manager;

public interface Manager<T> {
    T add(String key, T entry);
    T get(String key);
    T remove(String key);
}