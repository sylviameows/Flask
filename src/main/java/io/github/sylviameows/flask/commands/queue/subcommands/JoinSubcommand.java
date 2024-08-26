package io.github.sylviameows.flask.commands.queue.subcommands;

import com.mojang.brigadier.context.CommandContext;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.github.sylviameows.flask.commands.structure.types.GameArgumentType;
import io.github.sylviameows.flask.game.Game;
import io.github.sylviameows.flask.managers.PlayerManager;
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
        context.getSource().getSender().sendRichMessage("<red>Missing game argument.");
        return 1;
    }

    public int executeWithArgs(CommandContext<CommandSourceStack> context, Game game) {
        if (context.getSource().getSender() instanceof Player player) {
            if (!canJoin(player)) {
                player.sendRichMessage("<red>You are already occupied!");
                return 1;
            }
            game.getQueue().addPlayer(player);
        }
        return 1;
    }

    public boolean canJoin(Player player) {
        return !PlayerManager.instance().get(player).isOccupied();
    }
}
