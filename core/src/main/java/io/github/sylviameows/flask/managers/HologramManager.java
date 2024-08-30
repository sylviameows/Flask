package io.github.sylviameows.flask.managers;

import io.github.sylviameows.flask.hub.holograms.GameHologram;

public class HologramManager extends ManagerImpl<GameHologram> {
    private static final HologramManager instance = new HologramManager();
    public static HologramManager instance() {
        return instance;
    }

}
