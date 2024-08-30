package io.github.sylviameows.flask.commands.hologram.subcommands;

import com.mojang.brigadier.context.CommandContext;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.github.sylviameows.flask.commands.structure.types.GameArgumentType;
import io.github.sylviameows.flask.game.Game;
import io.github.sylviameows.flask.hub.holograms.GameHologram;
import io.github.sylviameows.flask.services.MessageService;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import org.bukkit.entity.Player;

@CommandProperties(label = "create", permission = "flask.hologram.create")
public class CreateSubcommand extends FlaskCommand {
    public CreateSubcommand() {
        super();

        arguments.add(Commands.argument("game", GameArgumentType.game()).executes(context -> {
            Game game = context.getArgument("game", Game.class);
            return executeWithArgs(context, game);
        }));
    }

    private int executeWithArgs(CommandContext<CommandSourceStack> context, Game game) {
        if (context.getSource().getSender() instanceof Player player) {
            var location = player.getLocation();
            new GameHologram(game, location);
            ms.sendMessage(player, MessageService.MessageType.STANDARD, "created_hologram", game.getSettings().getName());
            return 1;
        }

        // todo not player error
        return 0;
    }

    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        var source = context.getSource().getSender();
        ms.sendMessage(source, MessageService.MessageType.ERROR, "missing_arg", "game");
        return 1;
    }
}
