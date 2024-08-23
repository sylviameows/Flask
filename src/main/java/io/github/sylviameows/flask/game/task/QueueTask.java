package io.github.sylviameows.flask.game.task;

import io.github.sylviameows.flask.game.Queue;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.List;

public class QueueTask extends BukkitRunnable {
    private final Queue<?> parent;
    private final List<Player> players;
    private int count = 0;

    private final int max;
    private final int min;

    public QueueTask(Queue<?> queue, List<Player> players) {
        this.players = players;
        this.parent = queue;

        this.max = parent.getParent().getSettings().getMaxPlayers();
        this.min = parent.getParent().getSettings().getMinPlayers();
    }

    public boolean add(Player player) {
        if (players.size() >= parent.getParent().getSettings().getMaxPlayers()) {
            return false;
        }
        return players.add(player);
    }

    public boolean remove(Player player) {
        return players.remove(player);
    }

    public Integer size() {
        return players.size();
    }

    @Override
    public void run() {
        if (size() < min) {
            empty();
        }

        boolean full = players.size() >= max;
        if (count > 10 || full) {
            var lobby = parent.getParent().createLobby();
            for (Player player : players) {
                lobby.addPlayer(player);
            }
            this.clear();
        }

        count++;
    }

    private void clear() {
        players.clear();
        count = 0;
    }

    private void empty() {
        players.forEach(player -> {
            // todo: messaging
            parent.getQueue().addFirst(player);
        });

        this.cancel();
    }
}
