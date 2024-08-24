package io.github.sylviameows.flask.commands;

import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.LiteralCommandNode;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;

import java.util.ArrayList;
import java.util.List;

public abstract class FlaskCommand {
    String label;
    String[] aliases;
    String permission;

    List<FlaskCommand> subCommands = new ArrayList<>();
    List<ArgumentBuilder<CommandSourceStack, ?>> arguments = new ArrayList<>();

    protected FlaskCommand() {
        var annotation = this.getClass().getAnnotation(FlaskCommandSettings.class);

        label = annotation.label();
        aliases = annotation.aliases();
        permission = annotation.permission();
    }

    abstract public int execute(CommandContext<CommandSourceStack> context);

    public void addSubCommand(FlaskCommand command) {
        subCommands.add(command);
    }

    public LiteralArgumentBuilder<CommandSourceStack> preprocess(LiteralArgumentBuilder<CommandSourceStack> builder) {
        return builder;
    }

    public LiteralCommandNode<CommandSourceStack> build() {
        var command = Commands.literal(label);

        // for any special behaviour a command might need.
        command = preprocess(command);

        if (permission != null) {
            command.requires(source -> source.getSender().hasPermission(permission));
        }

        // add arguments
        if (!arguments.isEmpty()) {
            var chain = arguments.removeFirst();
            for (var argument : arguments) {
                chain = chain.then(argument);
            }
            chain = chain.executes(this::execute);
            command = command.then(chain);
        } else {
            command.executes(this::execute);
        }

        for (var subcommand : subCommands) {
            command = command.then(subcommand.build());
        }


        return command.build();
    }

    public void register(Commands commands) {
        commands.register(build(), List.of(aliases));
    }
}
