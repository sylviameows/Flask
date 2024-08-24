package io.github.sylviameows.flask.game;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.game.task.QueueTask;
import io.github.sylviameows.flask.managers.PlayerManager;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Queue<G extends Game> {
    private final G parent;

    private Integer totalPlayers;
    private final List<Player> queue;

    private QueueTask task;

    private final PlayerManager pm = PlayerManager.instance();

    public Queue(G game) {
        this.parent = game;
        this.totalPlayers = 0;
        this.queue = new ArrayList<>();
    }

    public void join(Player player) {
        pm.get(player).setGame(parent);
        totalPlayers++;

        Integer minimum = parent.getSettings().getMinPlayers();
        if (queue.size() + 1 >= minimum && (task == null || task.isCancelled())) {
            queue.add(player);

            task = new QueueTask(this, queue);
            task.runTaskTimer(parent.getPlugin(), 0L, 20L);
            queue.clear();
        } else if (task != null && !task.isCancelled()) {
            task.add(player);
        } else {
            queue.add(player);
        }
    }

    public void leave(Player player) {
        pm.remove(player).reset();
        queue.remove(player);

        if (task != null) task.remove(player);

        totalPlayers--;
    }

    public Integer getTotalPlayers() {
        return totalPlayers;
    }

    public Integer getQueueSize() {
        if (task != null && !task.isCancelled()) {
            return queue.size() + task.size();
        }
        return queue.size();
    }

    public Integer getPlayingCount() {
        return totalPlayers - getQueueSize();
    }

    public List<Player> getQueue() {
        return queue;
    }

    public G getParent() {
        return parent;
    }
}
