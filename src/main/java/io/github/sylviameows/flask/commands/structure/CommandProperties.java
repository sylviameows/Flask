package io.github.sylviameows.flask.commands.structure;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CommandProperties {
    String label();
    String permission() default "flask.commands";
    String[] aliases() default {};
}
