package io.github.sylviameows.flask.api.game;

import io.github.sylviameows.flask.api.Palette;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus;

/**
 * These are the base settings of a minigame, which tells Flask how to
 * handle the display and function of your minigame. It is recommended to
 * use the {@link SettingsBuilder} class to create this object which can
 * be created with the {@link Settings#builder()} method.
 */
public final class Settings {
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

    private Settings(String name, String description, TextColor color, Material material, Integer maxPlayers, Integer minPlayers) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.icon = material;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
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

    @ApiStatus.Internal
    public static Settings from(Game game) {
        GameProperties props = game.getClass().getAnnotation(GameProperties.class);
        if (props == null) {
            throw new IllegalArgumentException("Provided game does not contain properties needed.");
        }
        return new Settings(
                props.name(),
                props.description(),
                TextColor.color(props.color()),
                props.material(),
                props.min(),
                props.max()
        );
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
        private Integer minPlayers = 2;

        public SettingsBuilder() {}

        /**
         *  default value: "Unknown"
         */
        public SettingsBuilder setName(String name) {
            this.name = name;
            return this;
        }

        /**
         *  default value: ""
         */
        public SettingsBuilder setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         *  default value: {@link Palette#WHITE}
         */
        public SettingsBuilder setColor(TextColor color) {
            this.color = color;
            return this;
        }

        /**
         *  default value: {@link Material#ENDER_PEARL}
         */
        public SettingsBuilder setIcon(Material icon) {
            this.icon = icon;
            return this;
        }

        /**
         *  default value: 8
         */
        public SettingsBuilder setMaxPlayers(Integer max) {
            this.maxPlayers = max;
            return this;
        }

        /**
         *  default value: 2
         */
        public SettingsBuilder setMinPlayers(Integer min) {
            this.minPlayers = min;
            return this;
        }

        /**
         * @return a new {@link Settings} object with the specified settings.
         */
        public Settings build() {
            return new Settings(this);
        }
    }
}
