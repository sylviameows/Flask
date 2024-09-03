package io.github.sylviameows.flask.api.game;

import io.github.sylviameows.flask.api.FlaskAPI;
import io.github.sylviameows.flask.api.Palette;
import io.github.sylviameows.flask.api.services.MessageService;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class QueueTask extends BukkitRunnable {
    private final Queue<?> parent;
    private final List<Player> players;
    private int count = 0;
    private final int end = 10;

    private final int max;
    private final int min;

    private BossBar bar;

    public QueueTask(Queue<?> queue, List<Player> players) {
        this.players = new ArrayList<>(players); // clones the list
        this.parent = queue;

        this.max = parent.getParent().getSettings().getMaxPlayers();
        this.min = parent.getParent().getSettings().getMinPlayers();

        this.bar = BossBar.bossBar(
                Component.text("Starting in ").append(Component.text(end - count).color(Palette.MINT)).append(Component.text(" seconds...")),
                1f, BossBar.Color.BLUE, BossBar.Overlay.PROGRESS
        );

        players.forEach(p -> {
            p.showBossBar(bar);
        });
    }

    public boolean add(Player player) {
        if (players.size() >= parent.getParent().getSettings().getMaxPlayers()) {
            return false;
        }
        players.forEach(p -> {
            FlaskAPI.instance().getMessageService().sendMessage(p, MessageService.MessageType.QUEUE, "other_join", player.getName());
        });

        player.showBossBar(bar);
        return players.add(player);
    }

    public boolean remove(Player player) {
        player.hideBossBar(bar);
        var value = players.remove(player);
        players.forEach(p -> {
            FlaskAPI.instance().getMessageService().sendMessage(p, MessageService.MessageType.QUEUE, "other_leave", player.getName());
        });
        return value;
    }

    public int size() {
        return players.size();
    }

    @Override
    public void run() {
        if (size() < min) {
            empty();
        }

        boolean full = players.size() >= max;
        if (count >= end || full) {
            var game = parent.getParent();
            game.getPlugin().getComponentLogger().info("Creating Lobby for "+players);
            players.forEach(player -> player.hideBossBar(bar));
            game.createLobby(new ArrayList<>(players));
            this.clear();

            parent.fill(this); // attempts to fill queue after clearing, mainly used when maximum players is exceeded.
            return;
        } else {
            bar.name(Component.text("Starting in ").append(Component.text(end - count).color(Palette.MINT)).append(Component.text(" seconds...")));
            bar.progress(1 - ((float) count / end));
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
            player.hideBossBar(bar);
            player.showBossBar(parent.getBar());
            parent.updateBar();
        });

        this.cancel();
    }
}
