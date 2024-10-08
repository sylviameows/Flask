package io.github.sylviameows.flask.api.game;

import io.github.sylviameows.flask.api.FlaskAPI;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class Lobby<G extends Game> {
    protected final G parent;

    public List<Player> players;
    private @NotNull Phase phase;

    private World world;

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

        var api = parent.getPlugin().getFlaskAPI();
        players.forEach(player -> api.getPlayerManager().get(player).setLobby(this));

        this.phase = parent.initialPhase();
        Bukkit.getPluginManager().registerEvents(this.phase, parent.getPlugin());
        this.phase.onEnabled(this);
    }

    // todo: call function in phase
    public void addPlayer(Player player) {
        var api = parent.getPlugin().getFlaskAPI();
        api.getPlayerManager().get(player).setLobby(this);
        players.add(player);
        phase.onPlayerJoin(player);
    }

    public void removePlayer(Player player) {
        var api = parent.getPlugin().getFlaskAPI();
        api.getPlayerManager().get(player).setLobby(null);
        players.remove(player);
        phase.onPlayerLeave(player);
    }

    public void closeLobby() {
        phase.onDisabled();
        HandlerList.unregisterAll(phase);

        // todo: replace with a requeue feature?
        players.forEach(player -> {
            parent.getQueue().removePlayer(player);
            player.teleportAsync(FlaskAPI.instance().getSpawnLocation());
            player.setGameMode(GameMode.ADVENTURE);
            player.setHealth(20.0);
            player.setSaturation(10f);
            player.setFoodLevel(20);
        });

        Bukkit.getScheduler().runTaskLater(FlaskAPI.instance().getPlugin(), () -> {
            Bukkit.unloadWorld(world, false);
        }, 200L);
    }

    public void closeLobby(Consumer<Player> consumer) {
        players.forEach(consumer);
        closeLobby();
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
            parent.getPlugin().getComponentLogger().error("Next phase is not defined, aborting next phase action...");
            return;
        }
        updatePhase(nextPhase);
    }

    public G getParent() {
        return parent;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }
}
