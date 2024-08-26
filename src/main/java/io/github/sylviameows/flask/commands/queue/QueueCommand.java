package io.github.sylviameows.flask.commands.queue;

import com.mojang.brigadier.context.CommandContext;
import io.github.sylviameows.flask.commands.queue.subcommands.JoinSubcommand;
import io.github.sylviameows.flask.commands.queue.subcommands.LeaveSubcommand;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

@CommandProperties(label = "queue", aliases = {"q"}, permission = "flask.queue")
public class QueueCommand extends FlaskCommand {
    public QueueCommand() {
        super();
        subCommands.add(new JoinSubcommand());
        subCommands.add(new LeaveSubcommand());
    }

    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        // todo: provide data about your queue.
        return 0;
    }
}
