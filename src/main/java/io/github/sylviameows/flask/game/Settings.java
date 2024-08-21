package io.github.sylviameows.flask.game;

import io.github.sylviameows.flask.Palette;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;

public class Settings {
    // display options
    private final String name;
    private final String description;
    private final TextColor color;
    private final Material icon;

    // game options
    private final Integer maxPlayers;
    private final Integer minPlayers; // optional

    private Settings(SettingsBuilder builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.color = builder.color;
        this.icon = builder.icon;

        this.maxPlayers = builder.maxPlayers;
        this.minPlayers = builder.minPlayers;
    }

    public String getName() {
        return name;
    }
    public String getDescription() {
        return description;
    }
    public TextColor getColor() {
        return color;
    }
    public Material getIcon() {
        return icon;
    }

    public Integer getMaxPlayers() {
        return maxPlayers;
    }
    public Integer getMinPlayers() {
        return minPlayers;
    }

    public static SettingsBuilder builder() {
        return new SettingsBuilder();
    }

    public static Settings of(String name, String description, TextColor color, Material icon, Integer max, Integer min) {
        return new SettingsBuilder()
                .setName(name)
                .setDescription(description)
                .setColor(color)
                .setIcon(icon)
                .setMaxPlayers(max)
                .setMinPlayers(min)
                .build();
    }

    public static class SettingsBuilder {
        // display options
        private String name = "Unknown";
        private String description = "";
        private TextColor color = Palette.WHITE;
        private Material icon = Material.ENDER_PEARL;

        // game options
        private Integer maxPlayers = 8;
        private Integer minPlayers = 1;

        public SettingsBuilder() {}

        public SettingsBuilder setName(String name) {
            this.name = name;
            return this;
        }
        public SettingsBuilder setDescription(String description) {
            this.description = description;
            return this;
        }
        public SettingsBuilder setColor(TextColor color) {
            this.color = color;
            return this;
        }
        public SettingsBuilder setIcon(Material icon) {
            this.icon = icon;
            return this;
        }

        public SettingsBuilder setMaxPlayers(Integer max) {
            this.maxPlayers = max;
            return this;
        }
        public SettingsBuilder setMinPlayers(Integer min) {
            this.minPlayers = min;
            return this;
        }

        public Settings build() {
            return new Settings(this);
        }
    }
}
