package io.github.sylviameows.flask.hub.holograms.tasks;

import org.bukkit.Color;
import org.bukkit.entity.TextDisplay;
import org.bukkit.plugin.Plugin;

public class GameHologramShowTask extends GameHologramAnimationTask {
    private GameHologramShowTask(TextDisplay display, int tick, int end, float start, Plugin plugin) {
        super(display,tick,end,start, plugin);
    }
    public GameHologramShowTask(TextDisplay display, int end, Plugin plugin) {
        super(display, 0, end, display.getTransformation().getTranslation().y - 0.15f, plugin);
    }

    @Override
    public void run() {
        if (tick == 0) {
            initialize();
            return;
        }

        // easing calculations
        double ease = ease((double) tick / end);
        double opacity = 26 + ((256 - 26) * ease);
        double alpha = 26 + ((112 - 26) * ease);
        float change = 0.15f * (float) ease;

        // update entity
        updateHologram(opacity, alpha, change);

        // continue animation
        if (tick < end) {
            new GameHologramShowTask(display, tick + 1, end, start, plugin).runTaskLater(plugin, 1L);
        }
    }

    private void initialize() {
        display.setTextOpacity((byte) (26-128));
        display.setBackgroundColor(Color.fromARGB(26, 0, 0, 0));
        display.setTransformation(updateY(start));

        new GameHologramShowTask(display, tick + 1, end, start, plugin).runTaskLater(plugin, 1L);
    }
}
