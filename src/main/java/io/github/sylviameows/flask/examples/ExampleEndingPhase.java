package io.github.sylviameows.flask.examples;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.game.Lobby;
import io.github.sylviameows.flask.game.Phase;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class ExampleEndingPhase implements Phase {
    private Player winner;

    private Lobby<?> parent;

    public void setWinner(Player player) {
        this.winner = player;
    }

    @Override
    public void onEnabled(Lobby<?> parent) {
        this.parent = parent;

        parent.players.forEach(player -> player.sendMessage(Component.text(winner.getName()+" wins!")));
        // todo: close lobby after 5.
    }

    @Override
    public void onDisabled() {

    }

    @EventHandler
    private void damage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (parent.players.contains(player)) {
                event.setCancelled(true);
            }
        }
    }

    @Override
    public Phase next() {
        return null;
    }
}
