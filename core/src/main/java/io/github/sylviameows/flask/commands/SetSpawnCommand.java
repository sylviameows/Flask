package io.github.sylviameows.flask.commands;

import com.mojang.brigadier.context.CommandContext;
import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.api.services.MessageService;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.io.File;

@CommandProperties(label = "setspawn", permission = "flask.setspawn")
public class SetSpawnCommand extends FlaskCommand {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        var sender = context.getSource().getSender();
        Location location;
        try {
            location = ((Entity) sender).getLocation();
        } catch (Exception e) {
            ms.sendMessage(sender, MessageService.MessageType.ERROR, "not_player");
            return 0;
        }

        Flask.getInstance().getConfig().set("spawn_location", location);
        Flask.getInstance().saveConfig();

        ms.sendMessage(sender, MessageService.MessageType.STANDARD, "set_spawn");

        return 1;
    }
}
