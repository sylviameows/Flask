package io.github.sylviameows.flask.api.game;

import io.github.sylviameows.flask.api.Palette;
import io.github.sylviameows.flask.api.annotations.GameProperties;
import io.github.sylviameows.flask.api.game.map.GameMap;
import net.kyori.adventure.text.format.TextColor;
import org.bukkit.Material;
import org.jetbrains.annotations.ApiStatus;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * These are the base settings of a minigame, which tells Flask how to
 * handle the display and function of your minigame. It is recommended to
 * use the {@link SettingsBuilder} class to create this object which can
 * be created with the {@link Settings#builder()} method.
 */
public final class Settings<T extends GameMap> {
    // display options
    private final String name;
    private final String description;
    private final TextColor color;
    private final Material icon;

    // game options
    private final Integer maxPlayers;
    private final Integer minPlayers; // optional
    private final Class<T> mapClass;

    private Settings(SettingsBuilder<T> builder) {
        this.name = builder.name;
        this.description = builder.description;
        this.color = builder.color;
        this.icon = builder.icon;

        this.maxPlayers = builder.maxPlayers;
        this.minPlayers = builder.minPlayers;

        this.mapClass = fetchMapType(builder);
    }

    private Settings(String name, String description, TextColor color, Material material, Integer maxPlayers, Integer minPlayers, Class<T> mapProperties) {
        this.name = name;
        this.description = description;
        this.color = color;
        this.icon = material;
        this.maxPlayers = maxPlayers;
        this.minPlayers = minPlayers;
        this.mapClass = mapProperties;
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

    public Class<T> getMapClass() {
        return mapClass;
    }

    public T getFreshMap(String id) {
        try {
            var constructor = mapClass.getConstructor(String.class);
            if (constructor.trySetAccessible()) {
                return constructor.newInstance(id);
            }
        } catch (InvocationTargetException | NoSuchMethodException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    @ApiStatus.Internal
    public static <S extends GameMap> Settings<S> from(Game<S> game) {
        GameProperties props = game.getClass().getAnnotation(GameProperties.class);
        if (props == null) {
            throw new IllegalArgumentException("Provided game does not contain properties needed.");
        }

        Class<S> map = fetchMapType(game);
        return new Settings<>(
                props.name(),
                props.description(),
                TextColor.color(props.color()),
                props.material(),
                props.min(),
                props.max(),
                map
        );
    }

    @ApiStatus.Internal
    private static <S extends GameMap> Class<S> fetchMapType(Game<S> game) {
        return fetchMapType(game.getClass());
    }

    @ApiStatus.Internal
    private static <S extends GameMap> Class<S> fetchMapType(SettingsBuilder<S> builder) {
        return fetchMapType(builder.getClass());
    }

    @ApiStatus.Internal
    private static <S extends GameMap> Class<S> fetchMapType(Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();
        if (!(type instanceof ParameterizedType parameterizedType)) {
            throw new IllegalArgumentException("No parameterized type was found!");
        }

        var mapType = parameterizedType.getActualTypeArguments()[0];
        @SuppressWarnings("unchecked") // these classes should always match.
        Class<S> map = (Class<S>) mapType;
        return map;
    }

    public static <S extends GameMap> SettingsBuilder<S> builder() {
        return new SettingsBuilder<>();
    }

    public static <S extends GameMap> Settings<S> of(String name, String description, TextColor color, Material icon, Integer max, Integer min) {
        return new SettingsBuilder<S>()
                .setName(name)
                .setDescription(description)
                .setColor(color)
                .setIcon(icon)
                .setMaxPlayers(max)
                .setMinPlayers(min)
                .build();
    }

    public static class SettingsBuilder<T extends GameMap> {
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
        public SettingsBuilder<T> setName(String name) {
            this.name = name;
            return this;
        }

        /**
         *  default value: ""
         */
        public SettingsBuilder<T> setDescription(String description) {
            this.description = description;
            return this;
        }

        /**
         *  default value: {@link Palette#WHITE}
         */
        public SettingsBuilder<T> setColor(TextColor color) {
            this.color = color;
            return this;
        }

        /**
         *  default value: {@link Material#ENDER_PEARL}
         */
        public SettingsBuilder<T> setIcon(Material icon) {
            this.icon = icon;
            return this;
        }

        /**
         *  default value: 8
         */
        public SettingsBuilder<T> setMaxPlayers(Integer max) {
            this.maxPlayers = max;
            return this;
        }

        /**
         *  default value: 2
         */
        public SettingsBuilder<T> setMinPlayers(Integer min) {
            this.minPlayers = min;
            return this;
        }

        /**
         * @return a new {@link Settings} object with the specified settings.
         * @throws IllegalArgumentException if map properties were not set.
         */
        public Settings<T> build() {
            return new Settings<>(this);
        }
    }
}
