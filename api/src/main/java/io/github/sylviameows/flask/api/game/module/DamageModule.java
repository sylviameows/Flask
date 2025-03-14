package io.github.sylviameows.flask.api.game.module;

import io.github.sylviameows.flask.api.annotations.FlaskEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;
import java.util.List;

public class DamageModule extends FlaskModule {
    private boolean whitelist = false;
    private final List<EntityDamageEvent.DamageCause> affected = new ArrayList<>();

    public DamageModule(boolean whitelist) {
        this.whitelist = whitelist;
    }

    @FlaskEvent
    public void damage(EntityDamageEvent event) {
        boolean cancel = affected.contains(event.getCause());
        if (whitelist) {
            cancel = !cancel;
        }
        event.setCancelled(cancel);
    }

    public DamageModule add(EntityDamageEvent.DamageCause cause) {
        affected.add(cause);

        return this;
    }

    public DamageModule remove(EntityDamageEvent.DamageCause cause) {
        affected.remove(cause);

        return this;
    }

    public static DamageModule prevent(EntityDamageEvent.DamageCause... causes) {
        DamageModule module = new DamageModule(false);
        for (EntityDamageEvent.DamageCause cause : causes) {
            module.add(cause);
        }
        return module;
    }

    public static DamageModule allow(EntityDamageEvent.DamageCause... causes) {
        DamageModule module = new DamageModule(true);
        for (EntityDamageEvent.DamageCause cause : causes) {
            module.add(cause);
        }
        return module;
    }

    public static DamageModule invulnerable() {
        return new DamageModule(true);
    }
}
