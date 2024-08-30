package io.github.sylviameows.flask.commands.queue.subcommands;

import com.mojang.brigadier.context.CommandContext;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.github.sylviameows.flask.commands.structure.types.GameArgumentType;
import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.managers.PlayerManagerImpl;
import io.github.sylviameows.flask.services.MessageService;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

@CommandProperties(label = "join", permission = "flask.queue.join")
public class JoinSubcommand extends FlaskCommand {
    public JoinSubcommand() {
        super();

        arguments.add(Commands.argument("game", GameArgumentType.game()).executes(context -> {
            Game game = context.getArgument("game", Game.class);
            return executeWithArgs(context, game);
        }));
    }

    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        var source = context.getSource().getSender();
        ms.sendMessage(source, MessageService.MessageType.ERROR, "missing_arg", "game");
        return 1;
    }

    public int executeWithArgs(CommandContext<CommandSourceStack> context, Game game) {
        if (context.getSource().getSender() instanceof Player player) {
            if (!canJoin(player)) {
                ms.sendMessage(player, MessageService.MessageType.ERROR, "occupied");
                return 1;
            }

            ms.sendQueueMessage(player, "join", game);
            game.getQueue().addPlayer(player);
        }
        return 1;
    }

    public boolean canJoin(Player player) {
        return !PlayerManagerImpl.instance().get(player).isOccupied();
    }
}
