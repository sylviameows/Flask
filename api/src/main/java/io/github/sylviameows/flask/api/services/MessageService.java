package io.github.sylviameows.flask.api.services;

import io.github.sylviameows.flask.api.game.Game;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.ApiStatus;

/**
 * MessageService is not recommended to be used outside the api.
 */
@ApiStatus.Internal
public interface MessageService {
    enum MessageType {
        ERROR("errors."),
        STANDARD("messages."),
        QUEUE("messages.queue.");
        private String value;

        MessageType(String value) {
            this.value = value;
        }

        public String index() {
            return value;
        }
    }

    void sendMessage(CommandSender sender, MessageType type, String key);
    void sendMessage(CommandSender sender, MessageType type, String key, Object... params);
    void sendQueueMessage(CommandSender sender, String key, Game game);
}
