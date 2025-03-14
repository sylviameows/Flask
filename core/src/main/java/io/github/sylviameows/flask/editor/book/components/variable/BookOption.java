package io.github.sylviameows.flask.editor.book.components.variable;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.api.Palette;
import io.github.sylviameows.flask.api.annotations.MapProperty;
import io.github.sylviameows.flask.api.game.map.GameMap;
import io.github.sylviameows.flask.editor.book.components.BookComponent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.format.Style;
import net.kyori.adventure.text.format.TextDecoration;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Optional;
import java.util.UUID;

abstract public class BookOption implements BookComponent {
    private final String title;
    private final String description;
    private final boolean required;

    private final Field field;
    private final GameMap map;

    protected BookOption(Field field, GameMap map) {
        this.field = field;
        this.map = map;

        var properties = field.getAnnotation(MapProperty.class);
        if (properties == null) {
            throw new IllegalArgumentException("Field is not a valid map property.");
        }

        if (!properties.name().isEmpty()) {
            this.title = properties.name();
        } else {
            title = field.getName();
        }

        this.description = properties.description();
        required = !(field.getType() == Optional.class);

    }

    /**
     * The amount of lines this variable takes in the book.
     */
    public abstract int lines();

    protected Component header() {
        var component = Component.empty();

        var name = Component.text(title).style(Style.style(Palette.BLACK, TextDecoration.BOLD));
        if (!description.isEmpty()) {
            name = name.hoverEvent(HoverEvent.showText(Component.text(description)));
        }

        component = component.append(name);
        if (!isOptional()) {
            component = component.append(Component.text("*").hoverEvent(HoverEvent.showText(Component.text("Required"))));
        }

        return component;
    }

    abstract protected Component value();

    abstract protected Component buttons();

    protected boolean isOptional() {
        return getDefault() != null;
    }

    protected Object getDefault() {
        var clazz = map.getClass();
        try {
            var constructor = clazz.getConstructor(String.class);

            constructor.setAccessible(true);
            var instance = constructor.newInstance(UUID.randomUUID().toString());

            field.setAccessible(true);
            return field.get(instance);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException e) {
            Flask.logger().error(e.getClass().getName());
            return null;
        }
    }

    @Override
    public Component label() {
        return header().appendNewline().append(value()).appendNewline().append(buttons()).appendNewline();
    }

    protected String title() {
        return title;
    }

    protected String description() {
        return description;
    }

    protected boolean required() {
        return required;
    }

    protected Field field() {
        return field;
    }

    protected GameMap map() {
        return map;
    }
}
