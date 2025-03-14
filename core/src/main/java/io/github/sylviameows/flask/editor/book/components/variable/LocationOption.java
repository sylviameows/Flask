package io.github.sylviameows.flask.editor.book.components.variable;

import io.github.sylviameows.flask.api.Palette;
import io.github.sylviameows.flask.api.game.map.GameMap;
import io.github.sylviameows.flask.editor.book.components.button.BookButton;
import io.github.sylviameows.flask.editor.book.components.button.ButtonStyle;
import net.kyori.adventure.identity.Identity;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Field;

public class LocationOption extends BookOption {
    public LocationOption(Field field, GameMap map) {
        super(field, map);
    }

    @Override
    public int lines() {
        return 2;
    }

    @Override
    protected Component value() {
        try {
            field().setAccessible(true);
            var value = field().get(map());

            if (value instanceof Location location) {
                var string = String.format("%s, %s, %s", round(location.x()), round(location.y()), round(location.z()));
                return Component.text(string).color(Palette.DARK_GRAY);
            } else if (value == null) {
                return Component.text("* unset location *").color(Palette.DARK_GRAY);
            }

        } catch (IllegalAccessException ignored) {}
        return Component.text("* an error occurred *").color(Palette.RED_LIGHT);
    }

    private double round(double value) {
        return ((double) Math.round(value * 10)) / 10;
    }

    @Override
    protected Component buttons() {
        var set = BookButton.callback("set", "Click to set to your current location.", ButtonStyle.CONSTRUCTIVE, audience -> {
            var filtered = audience.get(Identity.UUID);
            if (filtered.isEmpty()) {
                return;
            }
            var player = Bukkit.getPlayer(filtered.get());
            if (player == null) {
                return;
            }

            try {
                field().setAccessible(true);
                field().set(map(), player.getLocation());
            } catch (IllegalAccessException ignored) {
            }
        });

        var teleport = BookButton.callback("teleport", "Click to teleport to set location.", ButtonStyle.MODIFYING, audience -> {
            var filtered = audience.get(Identity.UUID);
            if (filtered.isEmpty()) {
                return;
            }
            var player = Bukkit.getPlayer(filtered.get());
            if (player == null) {
                return;
            }

            try {
                field().setAccessible(true);
                player.teleport((Location) field().get(map()));
            } catch (IllegalAccessException ignored) {

            }
        });

        var unset = BookButton.callback("x", "Click to unset the location.", ButtonStyle.DESTRUCTIVE, audience -> {
            try {
                field().setAccessible(true);
                field().set(map(), null);
            } catch (IllegalAccessException ignored) {

            }
        });

        try {
            field().setAccessible(true);
            if (field().canAccess(map()) && field().get(map()) == null) {
                teleport.disable();
                unset.disable();
            }
        } catch (IllegalAccessException ignored) {

        }

        return Component.empty().append(set.label()).append(Component.space()).append(teleport.label()).append(Component.space()).append(unset.label());


    }
}
