package io.github.sylviameows.flask.api.game;

import org.bukkit.Material;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface GameProperties {
    String name();
    String description() default "";
    int color() default 0xFFFFFF;
    Material material() default Material.ENDER_PEARL;

    int min();
    int max();
}
