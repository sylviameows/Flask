package io.github.sylviameows.flask.commands.hologram.subcommands;

import com.mojang.brigadier.context.CommandContext;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.github.sylviameows.flask.managers.HologramManager;
import io.github.sylviameows.flask.services.MessageServiceImpl;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Interaction;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BlockIterator;

import java.util.Objects;

@CommandProperties(label = "delete", permission = "flask.hologram.delete")
public class DeleteSubcommand extends FlaskCommand {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getSender() instanceof Player player) {
            var target = getLookingAt(player, 3.0);
            if (target == null) {
                ms.sendMessage(player, MessageServiceImpl.MessageType.ERROR, "no_hologram_los");
                return 1;
            }

            var hologram = HologramManager.instance().get(target.getUniqueId().toString());
            if (hologram == null) {
                ms.sendMessage(player, MessageServiceImpl.MessageType.ERROR, "unknown"); // todo: better name
                return 1;
            }
            hologram.remove();
            ms.sendMessage(player, MessageServiceImpl.MessageType.STANDARD, "removed_hologram");

            return 1;
        }


        return 0;
    }

    private Entity getLookingAt(Player player, double range) {
        var entities = player.getNearbyEntities(range, range, range);
        var filtered = entities.stream().filter(entity -> {
            if (entity instanceof Interaction interaction) {
                String special = interaction.getPersistentDataContainer().get(new NamespacedKey("flask","special"), PersistentDataType.STRING);
                return Objects.equals(special, "hologram_interaction");
            }
            return false;
        }).toList();

        Entity target = null;
        BlockIterator iterator = new BlockIterator(player, (int) range);

        while (iterator.hasNext()) {
            Block block = iterator.next();
            int bX = block.getX();
            int bY = block.getY();
            int bZ = block.getZ();
            for (Entity entity : filtered) {
                Location location = entity.getLocation();
                double eX = location.x();
                double eY = location.y();
                double eZ = location.z();
                // (bx-.75 <= ex && ex <= bx+1.75) && (bz-.75 <= ez && ez <= bz+1.75) && (by-1 <= ey && ey <= by+2.5)
                if (
                    (bX-0.75 <= eX && eX <= bX+1.75) &&
                    (bZ-0.75 <= eZ && eZ <= bZ+1.75) &&
                    (bY-1.00 <= eY && eY <= bY+2.50)
                ) {
                    target = entity;
                    break;
                }
            }
        }
        return target;
    }
}
