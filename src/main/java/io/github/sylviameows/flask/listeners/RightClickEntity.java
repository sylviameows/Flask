package io.github.sylviameows.flask.listeners;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.game.Game;
import io.github.sylviameows.flask.managers.PlayerManager;
import io.github.sylviameows.flask.registries.GameRegistry;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

public class RightClickEntity implements Listener {
    public static void register(Flask plugin) {
        Bukkit.getPluginManager().registerEvents(new RightClickEntity(), plugin);
    }

    @EventHandler
    private void rightClickEntity(PlayerInteractEntityEvent event) {
        Entity target = event.getRightClicked();
        if (target instanceof Interaction interaction) {
            var pdc = interaction.getPersistentDataContainer();
            if (!Objects.equals(
                    pdc.get(new NamespacedKey("flask", "special"), PersistentDataType.STRING),
                    "hologram_interaction")
            ) return;

            Player player = event.getPlayer();
            if (PlayerManager.instance().get(player).isOccupied()) {
                player.sendMessage("You are already occupied!");
                return;
            }

            String gameKeyString = pdc.get(new NamespacedKey("flask", "game"), PersistentDataType.STRING);
            if (gameKeyString == null) return;
            Game game = GameRegistry.instance().get(NamespacedKey.fromString(gameKeyString));
            if (game == null) {
                player.sendMessage("Could not find the game \""+gameKeyString+"\", if you believe this is an error contact your server's administrator."); // TODO: service?
                return;
            }

            // add player to queue
            game.getQueue().addPlayer(player);
            player.sendMessage(
                    Component.text("Joining ").append(
                            Component.text(game.getSettings().getName())
                                    .color(game.getSettings().getColor())
                    )
            );
        }
    }
}
