package io.github.sylviameows.flask.hub.holograms.tasks;

import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.Plugin;

public class GameHologramHideTask extends GameHologramAnimationTask {
    private GameHologramHideTask(TextDisplay display, int tick, int end, float start, Plugin plugin) {
        super(display,tick,end,start, plugin);
    }
    public GameHologramHideTask(TextDisplay display, int end, Plugin plugin) {
        super(display, 0, end, display.getTransformation().getTranslation().y - 0.15f, plugin);
    }

    @Override
    public void run() {
        // easing calculations
        double ease = ease((double) tick / end);
        double opacity = 256 - (250 * ease);
        double alpha = 112 - (112*ease);
        float change = 0.15f * (float)ease;

        // update entity
        updateHologram(opacity, alpha, change);

        // continue animation
        if (tick < end) {
            new GameHologramHideTask(display, tick + 1, end, start, plugin).runTaskLater(plugin, 1L);
        } else {
            display.remove();
        }
    }
}
