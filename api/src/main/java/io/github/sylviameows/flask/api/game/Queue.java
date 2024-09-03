package io.github.sylviameows.flask.api.game;

import io.github.sylviameows.flask.api.FlaskAPI;
import io.github.sylviameows.flask.api.Palette;
import io.github.sylviameows.flask.api.manager.PlayerManager;
import io.github.sylviameows.flask.api.FlaskPlayer;
import io.github.sylviameows.flask.api.services.MessageService;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * Manages the players that join a game and sends them to a lobby when one is
 * available and meets the requirements in the game's {@link Settings}.
 * @param <G> the {@link Game} type this queue is meant for.
 */
public class Queue<G extends Game> {
    private final G parent;

    private Integer totalPlayers;
    private final List<Player> queue;

    private QueueTask task;

    private final PlayerManager pm;

    private BossBar bar;

    public Queue(G game) {
        this.parent = game;
        this.totalPlayers = 0;
        this.queue = new ArrayList<>();
        this.pm = parent.getPlugin().getFlaskAPI().getPlayerManager();

        this.bar = BossBar.bossBar(Component.text("Waiting for ")
                .append(Component.text(parent.getSettings().getMinPlayers()).color(Palette.MINT))
                .append(Component.text(" more player(s)...")), 0f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS);
    }

    /**
     * Adds a player into the queue. Additionally, updates the {@link FlaskPlayer} instance of this player.
     */
    public void addPlayer(Player player) {
        pm.get(player).setGame(parent);
        totalPlayers++;

        Integer minimum = parent.getSettings().getMinPlayers();
        Integer maximum = parent.getSettings().getMaxPlayers();

        queue.forEach(p -> {
            FlaskAPI.instance().getMessageService().sendMessage(p, MessageService.MessageType.QUEUE, "other_join", player.getName());
        });

        if (queue.size() + 1 >= minimum && (task == null || task.isCancelled())) {
            queue.add(player);

            var players = queue.subList(0, Math.min(maximum, queue.size()));
            players.forEach(p -> p.hideBossBar(bar));
            task = new QueueTask(this, players);
            task.runTaskTimer(parent.getPlugin(), 0L, 20L);
            queue.removeAll(players);
        } else if (task != null && !task.isCancelled() && task.size() < maximum) {
            task.add(player);
        } else {
            queue.add(player);
            updateBar();
            player.showBossBar(bar);
        }
    }

    public void updateBar() {
        Integer minimum = parent.getSettings().getMinPlayers();
        bar.progress((float) queue.size() / minimum);

        bar.name(Component.text("Waiting for ")
                .append(Component.text(parent.getSettings().getMinPlayers() - queue.size()).color(Palette.MINT))
                .append(Component.text(" more player(s)...")));
    }

    /**
     * Removes a player from the queue. Additionally, updates the {@link FlaskPlayer} instance of this player.
     */
    public void removePlayer(Player player) {
        FlaskPlayer flaskPlayer = pm.get(player);
        if (flaskPlayer.getGame() == this.parent) {
            flaskPlayer.setGame(null);
            queue.remove(player);
            queue.forEach(p -> {
                FlaskAPI.instance().getMessageService().sendMessage(p, MessageService.MessageType.QUEUE, "other_leave", player.getName());
            });
            player.hideBossBar(bar);
            updateBar();
            if (task != null) task.remove(player);
            totalPlayers--;
        }
    }

    /**
     * Fills a {@link QueueTask} with players from the queue until it is full, or the queue runs out of players.
     * @param task the {@link QueueTask} to fill.
     */
    public void fill(@NotNull QueueTask task) {
        Integer maximum = parent.getSettings().getMaxPlayers();

        while (task.size() < maximum && !queue.isEmpty()) {
            task.add(queue.removeFirst());
        }
    }

    /**
     * @return the total number of players in this game (includes the players in queue).
     */
    public Integer getTotalPlayers() {
        return totalPlayers;
    }

    /**
     * @return the number of players in the queue, and not actively in a lobby.
     */
    public Integer getQueueSize() {
        if (task != null && !task.isCancelled()) {
            return queue.size() + task.size();
        }
        return queue.size();
    }

    /**
     * @return the number of players actively in a lobby, and not in a queue.
     */
    public Integer getPlayingCount() {
        return totalPlayers - getQueueSize();
    }

    public List<Player> getQueue() {
        return queue;
    }

    public G getParent() {
        return parent;
    }

    public BossBar getBar() {
        return bar;
    }
}
