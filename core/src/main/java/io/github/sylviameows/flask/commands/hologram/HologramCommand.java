package io.github.sylviameows.flask.commands.hologram;

import com.mojang.brigadier.context.CommandContext;
import io.github.sylviameows.flask.commands.hologram.subcommands.CreateSubcommand;
import io.github.sylviameows.flask.commands.hologram.subcommands.DeleteSubcommand;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;

@CommandProperties(label = "hologram", aliases = {"holo"}, permission = "flask.hologram")
public class HologramCommand extends FlaskCommand {
    public HologramCommand() {
        super();
        subCommands.add(new CreateSubcommand());
        subCommands.add(new DeleteSubcommand());
    }

    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        // todo: provide help information
        return 0;
    }
}
