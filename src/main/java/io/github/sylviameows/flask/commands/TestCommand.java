package io.github.sylviameows.flask.commands;

import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.resolvers.selector.PlayerSelectorArgumentResolver;

@FlaskCommandSettings(label = "testing", aliases = {"test","t"})
public class TestCommand extends FlaskCommand {
    public TestCommand() {
        super();
        arguments.add(Commands.argument("user", ArgumentTypes.player()));
    }

    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        try {
            var user = context.getArgument("user", PlayerSelectorArgumentResolver.class).resolve(context.getSource());
            context.getSource().getSender().sendRichMessage("<rainbow>you chose "+user.getFirst().getName());
            return 1;
        } catch (CommandSyntaxException e) {
            return 0;
        }


    }
}
