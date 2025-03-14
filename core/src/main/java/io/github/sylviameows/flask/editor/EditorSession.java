package io.github.sylviameows.flask.editor;

import com.infernalsuite.aswm.api.world.SlimeWorld;
import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.api.Palette;
import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.api.game.map.GameMap;
import io.github.sylviameows.flask.api.services.WorldService;
import io.github.sylviameows.flask.editor.book.EditorBook;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;

public class EditorSession<T extends GameMap> implements Listener {
    private final Player manager;
    private final ArrayList<Player> editors = new ArrayList<>();

    private SlimeWorld slime;
    private World world;

    private final T map;
    private final Game<T> game;

    private final WorldService ws = Flask.getInstance().getWorldService();

    private EditorBook book;

    private final NamespacedKey ITEM_KEY = new NamespacedKey(Flask.getInstance().getPlugin(), "editor_item");

    public EditorSession(Player player, Game<T> game, String id) {
        this.manager = player;
        editors.add(player);

        this.game = game;

        var mm = game.getMapManager();

        if (mm.get(id) != null) {
            this.map = mm.get(id);
        } else {
            this.map = game.getSettings().getFreshMap(id);
            mm.add(id, this.map);
        }

        assert map != null;

        mm.getWorld(map.getId()).whenComplete((w, ex) -> {
            if (ex != null || w == null) {
                throw new RuntimeException(ex);
            }
            this.slime = w;
            this.world = Bukkit.getWorld(slime.getName());

            postWorld();
        });

        init();
    }

    public EditorSession(Player player, Game<T> game, T map) {
        this.manager = player;
        editors.add(player);

        this.game = game;
        this.map = map;

        var mm = game.getMapManager();
        mm.getWorld(map).whenComplete((w, ex) -> {
            if (ex != null || w == null) {
                return;
            }
            this.slime = w;
            this.world = Bukkit.getWorld(slime.getName());

            postWorld();
        });

        init();
    }

    public Game<T> getGame() {
        return game;
    }

    public T getMap() {
        return map;
    }

    private void init() {
        book = new EditorBook(game, map);

        // registers our editor session events, unregistered when session closes.
        Bukkit.getPluginManager().registerEvents(this, Flask.getInstance().getPlugin());
    }

    private void postWorld() {
        manager.teleportAsync(world.getSpawnLocation()).whenComplete((success, ex) -> {
            if (ex != null) {
                Flask.logger().warn(ex.getMessage());
                return;
            }
            postTeleport(manager);
        });
    }

    private void postTeleport(Player player) {
        player.setGameMode(GameMode.CREATIVE);

        var editor_item = new ItemStack(Material.WRITTEN_BOOK);
        editor_item.editMeta(meta -> {
            meta.displayName(Component.text("Editor Utilities").color(Palette.MINT).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE));
            meta.lore(List.of(Component.text("use this item to edit the properties of the map").color(Palette.WHITE).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE),
                    Component.text(""),
                    Component.text("ID: " + map.getId()).color(Palette.DARK_GRAY).decorationIfAbsent(TextDecoration.ITALIC, TextDecoration.State.FALSE)
            ));

            meta.setEnchantmentGlintOverride(true);

            meta.getPersistentDataContainer().set(ITEM_KEY, PersistentDataType.BOOLEAN, true);
        });

        player.getInventory().setItem(8, editor_item);
    }

    public void save() {
        ws.saveWorld(slime);
    }

    public void close() {
        ws.saveWorld(slime).whenComplete((success, ex) -> {
            Bukkit.unloadWorld(world, false);
        });

        // unregisters our events for this session
        HandlerList.unregisterAll(this);
        world.getPlayers().forEach(p -> p.teleportAsync(Flask.getInstance().getSpawnLocation()));
    }

    @EventHandler
    private void click(PlayerInteractEvent event) {
        if (event.getHand() != EquipmentSlot.HAND) {
            return;
        }
        if (!(event.getAction() == Action.RIGHT_CLICK_AIR || event.getAction() == Action.RIGHT_CLICK_BLOCK)) {
            return;
        }
        if (!editors.contains(event.getPlayer())) {
            return;
        }

        var item = event.getItem();
        if (item == null) {
            return;
        }
        if (item.getPersistentDataContainer().getOrDefault(ITEM_KEY, PersistentDataType.BOOLEAN, false)) {
            book.open(event.getPlayer());
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void swapHands(PlayerSwapHandItemsEvent event) {
        if (!editors.contains(event.getPlayer())) {
            return;
        }

        var item = event.getOffHandItem();
        // stops player from swapping hands with their editor item
        if (item.getPersistentDataContainer().getOrDefault(ITEM_KEY, PersistentDataType.BOOLEAN, false)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void clickItem(InventoryClickEvent event) {
        if (event.getWhoClicked() instanceof Player player) {
            if (!editors.contains(player)) {
                return;
            }
        } else {
            return;
        }

        var item = event.getCurrentItem();
        if (item == null) {
            return;
        }

        // stops player from changing their editor items slot.
        if (item.getPersistentDataContainer().getOrDefault(ITEM_KEY, PersistentDataType.BOOLEAN, false)) {
            player.setItemOnCursor(null);
            event.setCancelled(true);
        }
    }

    @EventHandler
    private void dropItem(PlayerDropItemEvent event) {
        if (!editors.contains(event.getPlayer())) {
            return;
        }

        // cancel all item dropping in editor, maybe make custom /item send command to share with other editors?
        event.setCancelled(true);
    }

    // public void addEditor(Player player) {
    //     player.teleport(new Location(world, 0.5, 1.0, 0.5));
    // }

}
