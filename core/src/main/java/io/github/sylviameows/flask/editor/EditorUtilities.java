package io.github.sylviameows.flask.editor;

import io.github.sylviameows.flask.api.FlaskPlayer;
import io.github.sylviameows.flask.api.annotations.MapProperty;
import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.api.game.map.GameMap;
import io.github.sylviameows.flask.players.FlaskPlayerImpl;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;

public final class EditorUtilities {
    private EditorUtilities() {}

    public static ArrayList<Field> getMapProperties(Game<? extends GameMap> game) {
        return getMapProperties(game.getSettings().getMapClass());
    }

    public static ArrayList<Field> getMapProperties(Class<? extends GameMap> map) {
        return getFieldsMatchingAnnotation(map, MapProperty.class);
    }

    private static ArrayList<Field> getFieldsMatchingAnnotation(Class<?> clazz, Class<? extends Annotation> annotation) {
        ArrayList<Field> fields = new ArrayList<>();

        for (Field field : clazz.getDeclaredFields()) {
            if (!field.isAnnotationPresent(annotation)) {
                continue;
            }
            fields.add(field);
        }

        var superClass = clazz.getSuperclass();
        if (superClass == null) {
            return fields;
        } else {
            var superClassProps = getFieldsMatchingAnnotation(superClass, annotation);
            superClassProps.addAll(fields);
            return superClassProps;
        }
    }

    public static void setSession(FlaskPlayer player, EditorSession<? extends GameMap> session) {
        ((FlaskPlayerImpl) player).setSession(session);
    }

    public static EditorSession<? extends GameMap> getSession(FlaskPlayer player) {
        return ((FlaskPlayerImpl) player).getSession();
    }

}
