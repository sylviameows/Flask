package io.github.sylviameows.flask.game;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class Lobby<G extends Game> {
    protected final G parent;

    public ArrayList<Player> players;
    private Phase phase;

    public Lobby(G parent) {
        this.parent = parent;
        this.phase = parent.initialPhase();
    }


    public Phase getPhase() {
        return phase;
    }
    public void setPhase(@NotNull Phase phase) {
        if (this.phase != null) {
            this.phase.onDisabled();

            HandlerList.unregisterAll(this.phase);
        }

        this.phase = phase;
        this.phase.onEnabled(this);

        Bukkit.getPluginManager().registerEvents(this.phase, parent.getPlugin());
    }

    public void nextPhase() {
        setPhase(this.phase.next());
    }
}
