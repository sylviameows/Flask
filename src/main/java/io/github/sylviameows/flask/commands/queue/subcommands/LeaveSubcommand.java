package io.github.sylviameows.flask.commands.queue.subcommands;

import com.mojang.brigadier.context.CommandContext;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.github.sylviameows.flask.managers.PlayerManager;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

@CommandProperties(label = "leave", permission = "flask.queue.leave")
public class LeaveSubcommand extends FlaskCommand {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getSender() instanceof Player player) {
            var flaskPlayer = PlayerManager.instance().get(player);

            if (!flaskPlayer.isInQueue()) {
                player.sendRichMessage("<red>You are not in a queue!");
                return 1;
            }

            flaskPlayer.getGame().getQueue().removePlayer(player);
        }

        return 0;
    }
}
