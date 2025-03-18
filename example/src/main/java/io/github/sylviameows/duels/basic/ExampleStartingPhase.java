package io.github.sylviameows.duels.basic;

import com.infernalsuite.asp.api.world.SlimeWorld;
import io.github.sylviameows.flask.api.FlaskAPI;
import io.github.sylviameows.flask.api.game.phase.ListenerPhase;
import io.github.sylviameows.flask.api.game.phase.Phase;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.util.Vector;

import java.util.concurrent.ExecutionException;

public class ExampleStartingPhase extends ListenerPhase {
    private Player playerA;
    private Player playerB;

    @Override
    public void enabled() {
        playerA = getLobby().getPlayers().getFirst();
        playerB = getLobby().getPlayers().getLast();

        setupPlayer(playerA, Color.RED);
        setupPlayer(playerB, Color.AQUA);

        var promise = FlaskAPI.instance().getWorldService().findAndUseTemplate("example");
        Bukkit.getScheduler().runTaskAsynchronously(getLobby().getParent().getPlugin(), task -> {
            SlimeWorld slimeWorld;
            try {
                slimeWorld = promise.get();
            } catch (InterruptedException | ExecutionException e) {
                getLobby().closeLobby(player -> {
                    player.sendRichMessage("<red>Template doesnt exist.");
                    // todo: Flask.getMessageService().sendMessage(player, MessageService.MessageType.ERROR, "template_doesnt_exist");
                });
                return;
            }

            var world = Bukkit.getWorld(slimeWorld.getName());
            getLobby().setWorld(world);
            var center = new Location(world, 0.0, 64.0, 0.0);

            var a = center.clone().add(0.0, 0.0, 15.0).setDirection(new Vector(0,0,-1));
            var b = center.clone().add(0.0, 0.0, -15.0).setDirection(new Vector(0,0,1));

            playerA.teleportAsync(a);
            playerB.teleportAsync(b);

            getLobby().nextPhase();
        });
    }

    private void setupPlayer(Player player, Color color) {
        var inventory = player.getInventory();
        inventory.clear();
        inventory.setItem(0, new ItemStack(Material.WOODEN_SWORD));

        var chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
        var meta = (LeatherArmorMeta) chestplate.getItemMeta();
        meta.setColor(color);
        chestplate.setItemMeta(meta);
        inventory.setItem(EquipmentSlot.CHEST, chestplate);

        player.setGameMode(GameMode.ADVENTURE);
        player.setHealth(20.0);
    }

    @Override
    public Phase next() {
        return new CountdownPhase(playerA, playerB);
    }
}
