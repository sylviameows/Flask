package io.github.sylviameows.flask.examples;

import io.github.sylviameows.flask.game.Lobby;
import io.github.sylviameows.flask.game.Phase;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

public class ExampleStartingPhase implements Phase {
    private Player playerA;
    private Player playerB;

    @Override
    public void onEnabled(Lobby<?> parent) {
        playerA = parent.players.getFirst();
        playerB = parent.players.getLast();

        setupPlayer(playerA, Color.RED);
        setupPlayer(playerB, Color.AQUA);

        parent.nextPhase();
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
    public void onDisabled() {
        // todo: teleport
    }

    @Override
    public Phase next() {
        return new ExamplePlayingPhase(playerA, playerB);
    }
}
