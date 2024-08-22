package io.github.sylviameows.flask.game;

import io.github.sylviameows.flask.managers.PlayerManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Queue<G extends Game> {
    private final G parent;

    private Integer totalPlayers;
    private List<Player> queue;

    private PlayerManager pm = PlayerManager.instance();

    public Queue(G game) {
        this.parent = game;
        this.totalPlayers = 0;
        this.queue = new ArrayList<>();
    }

    public void join(Player player) {
        pm.get(player).setGame(parent);
        queue.add(player);
        totalPlayers++;
    }

    public void leave(Player player) {
        pm.remove(player).reset();
        queue.remove(player);
        totalPlayers--;
    }

    public Integer getTotalPlayers() {
        return totalPlayers;
    }

    public Integer getSizeOfQueue() {
        return queue.size();
    }

    public Integer getPlayingCount() {
        return totalPlayers - queue.size();
    }

    // todo: queue task?
}
