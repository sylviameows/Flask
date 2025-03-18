package io.github.sylviameows.flask.commands.editor.session.management;

import com.mojang.brigadier.context.CommandContext;
import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.api.FlaskPlayer;
import io.github.sylviameows.flask.api.game.map.GameMap;
import io.github.sylviameows.flask.api.services.MessageService;
import io.github.sylviameows.flask.commands.structure.CommandProperties;
import io.github.sylviameows.flask.commands.structure.FlaskCommand;
import io.github.sylviameows.flask.editor.EditorSession;
import io.github.sylviameows.flask.editor.EditorUtilities;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import org.bukkit.entity.Player;

@CommandProperties(label = "close", aliases = {"end"})
public class CloseSession extends FlaskCommand {
    @Override
    public int execute(CommandContext<CommandSourceStack> context) {
        if (context.getSource().getSender() instanceof Player player) {
            FlaskPlayer flaskPlayer = Flask.getInstance().getPlayerManager().get(player);
            EditorSession<? extends GameMap> session = EditorUtilities.getSession(flaskPlayer);

            if (session == null) {
                ms.sendMessage(player, MessageService.MessageType.EDITOR, "no_session");
                return 1;
            }

            var mapId = session.getMap().getId();

            EditorUtilities.setSession(flaskPlayer, null);
            session.close();

            ms.sendMessage(player, MessageService.MessageType.EDITOR, "session.close", mapId);

            return 1;
        }

        ms.sendMessage(context.getSource().getSender(), MessageService.MessageType.ERROR, "not_player");
        return 1;
    }
}
