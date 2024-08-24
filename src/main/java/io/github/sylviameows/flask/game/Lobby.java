package io.github.sylviameows.flask.game;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.managers.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class Lobby<G extends Game> {
    protected final G parent;

    public List<Player> players;
    private @NotNull Phase phase;

    public Lobby(G parent) {
        this.parent = parent;
        this.players = new ArrayList<>();

        this.phase = parent.initialPhase();
        Bukkit.getPluginManager().registerEvents(this.phase, parent.getPlugin());
        this.phase.onEnabled(this);
    }

    public Lobby(G parent, List<Player> players) {
        this.parent = parent;
        this.players = players;

        this.phase = parent.initialPhase();
        this.phase.onEnabled(this);
        Bukkit.getPluginManager().registerEvents(this.phase, parent.getPlugin());
    }

    // todo: call function in phase
    public void addPlayer(Player player) {
        PlayerManager.instance().get(player).setLobby(this);
        players.add(player);
    }

    public void removePlayer(Player player) {
        PlayerManager.instance().get(player).setLobby(null);
        players.remove(player);
    }


    public Phase getPhase() {
        return phase;
    }
    public void updatePhase(@NotNull Phase phase) {
        this.phase.onDisabled();
        HandlerList.unregisterAll(this.phase);

        this.phase = phase;

        this.phase.onEnabled(this);
        Bukkit.getPluginManager().registerEvents(this.phase, parent.getPlugin());
    }

    public void nextPhase() {
        Phase nextPhase = phase.next();
        if (nextPhase == null) {
            Flask.logger.error("Next phase is not defined, aborting next phase action...");
            return;
        }
        updatePhase(nextPhase);
    }
}
