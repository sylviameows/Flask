package io.github.sylviameows.flask.examples;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.game.Lobby;
import io.github.sylviameows.flask.game.Phase;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

public class ExamplePlayingPhase implements Phase {
    private final Player playerA;
    private final Player playerB;

    private final ExampleEndingPhase next;

    private Lobby<?> parent;

    public ExamplePlayingPhase(Player playerA, Player playerB) {
        this.playerA = playerA;
        this.playerB = playerB;

        this.next = new ExampleEndingPhase();
    }

    @Override
    public void onEnabled(Lobby<?> parent) {
        this.parent = parent;
    }

    @Override
    public void onDisabled() {

    }
    @Override
    public void onPlayerLeave(Player player) {
        if (player == playerA) next.setWinner(playerB);
        else next.setWinner(playerA);
        parent.nextPhase();
    }

    @EventHandler
    private void onDeath(PlayerDeathEvent event) {
        var player = event.getPlayer();
        if (player == playerA) {
            next.setWinner(playerB);
            event.setCancelled(true);
            player.setHealth(20.0);
            parent.nextPhase();
        } else if (player == playerB) {
            next.setWinner(playerA);
            event.setCancelled(true);
            player.setHealth(20.0);
            parent.nextPhase();
        }
    }

    @Override
    public Phase next() {
        return next;
    }
}
