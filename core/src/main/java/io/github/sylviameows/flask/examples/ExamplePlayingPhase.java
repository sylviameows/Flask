package io.github.sylviameows.flask.examples;

import io.github.sylviameows.flask.api.game.Lobby;
import io.github.sylviameows.flask.api.game.Phase;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.jetbrains.annotations.NotNull;

public class ExamplePlayingPhase implements Phase {
    private final Player playerA;
    private final Player playerB;

    private final @NotNull ExampleEndingPhase nextPhase;

    private Lobby<?> parent;

    public ExamplePlayingPhase(Player playerA, Player playerB) {
        this.playerA = playerA;
        this.playerB = playerB;

        this.nextPhase = new ExampleEndingPhase();
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
        if (player == playerA) nextPhase.setWinner(playerB);
        else nextPhase.setWinner(playerA);
        parent.nextPhase();
    }

    @EventHandler
    private void onDeath(EntityDeathEvent event) {
        if (event.getEntity() instanceof Player player) {
            if (player == playerA) {
                nextPhase.setWinner(playerB);
                parent.nextPhase();
            } else if (player == playerB) {
                nextPhase.setWinner(playerA);
                parent.nextPhase();
            } else {
                return;
            }
            event.setCancelled(true);
            player.setHealth(20.0);
            player.setGameMode(GameMode.SPECTATOR);
        }
    }

    @Override
    public Phase next() {
        return nextPhase;
    }
}
