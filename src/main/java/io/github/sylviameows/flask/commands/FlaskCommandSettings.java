package io.github.sylviameows.flask.commands;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface FlaskCommandSettings {
    String label();
    String permission() default "flask.commands";
    String[] aliases() default {};
}
