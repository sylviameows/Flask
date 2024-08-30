package io.github.sylviameows.flask.api;

import io.github.sylviameows.flask.api.manager.PlayerManager;
import io.github.sylviameows.flask.api.registry.GameRegistry;
import io.github.sylviameows.flask.api.services.WorldService;
import net.kyori.adventure.util.Services;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.ApiStatus;

public interface FlaskAPI {
    GameRegistry getGameRegistry();
    PlayerManager getPlayerManager();
    WorldService getWorldService();

    Plugin getPlugin();

    static FlaskAPI instance() {
        return FlaskAPI.Holder.INSTANCE;
    }

    @ApiStatus.Internal
    public static class Holder {
        private static FlaskAPI INSTANCE;
        public static void setInstance(FlaskAPI api) {
            INSTANCE = api;
        }

        public Holder() {
        }
    }
}