package io.github.sylviameows.flask.commands;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;
import org.bukkit.entity.Player;

@CommandProperties(label = "testing", aliases = {"test","t"})
public class TestCommand extends FlaskCommand {
    public TestCommand() {
        super();
        arguments.add(Commands.argument("user", ArgumentTypes.player()).executes(ctx -> {

            return 1;
        }));
    }

    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        var input = context.getArgument("user", PlayerSelectorArgumentResolver.class);
        if (input == null) {
            context.getSource().getSender().sendRichMessage("did not provide a user.");
            return 1;
        }

        try {
            Player player = input.resolve(context.getSource()).getFirst();
            context.getSource().getSender().sendRichMessage("<rainbow>you chose "+player.getName());
        } catch (CommandSyntaxException e) {
            context.getSource().getSender().sendRichMessage("an error occured finding the selected user.");
            return 0;
        }
        return 1;
    }
}
