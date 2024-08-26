package io.github.sylviameows.flask.services;

import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.game.Game;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.serializer.ComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class MessageService {
    private final FileConfiguration config;
    private final Map<String,String> colors = new HashMap<>();
    private ComponentSerializer<Component, ?, String> serializer;

    private Component prefix = Component.empty();
    private Component errorPrefix = Component.empty();

    public MessageService(Flask flask) {
        config = getConfig(flask);


        init();
    }

    private FileConfiguration getConfig(Flask flask) {
        File file = new File(flask.getDataFolder(), "messages.yml");
        if (!file.exists()) {
            file.getParentFile().mkdirs();
            flask.saveResource("messages.yml", false);
        }

        return YamlConfiguration.loadConfiguration(file);
    }

    private void init() {
        var format = config.getString("format");
        if (format != null && format.equalsIgnoreCase("legacy")) {
            serializer = LegacyComponentSerializer.legacyAmpersand();
        } else {
            serializer = MiniMessage.miniMessage();
        }

        var colors = config.getConfigurationSection("colors");
        if (colors != null) {
            for (var key : colors.getKeys(false)) {
                String value = colors.getString(key);
                this.colors.put(key, value);
            }
        }

        if (config.getBoolean("prefix.enabled", true)) {
            String rawDefault = config.getString("prefix.default", "<color:#87ffdf>🧪 › ");
            prefix = serializer.deserialize(convertTemplates(rawDefault));

            String rawError = config.getString("prefix.error", "<red>🧪 › ");
            errorPrefix = serializer.deserialize(convertTemplates(rawError));
        }
    }

    private String convertTemplates(String s) {
        String result = s;
        for (Map.Entry<String,String> entry : colors.entrySet()) {
            result = result.replaceAll("\\$"+entry.getKey(), entry.getValue());
        }
        return result;
    }

    private String getMessage(MessageType type, String key) {
        String location = type.index()+key;
        var base = config.getString(location,"<red>Undefined message "+location);
        return convertTemplates(base);
    }

    private void sendMessage(CommandSender sender, String raw) {
        sendRawMessage(sender, MessageType.STANDARD, raw);
    }

    private void sendRawMessage(CommandSender sender, MessageType type, String raw) {
        Component component = serializer.deserialize(raw);
        component = switch (type) {
            case ERROR -> errorPrefix.append(component);
            default -> prefix.append(component);
        };

        sender.sendMessage(component);
    }

    public void sendMessage(CommandSender sender, MessageType type, String key) {
        sendRawMessage(sender, type, getMessage(type, key));
    }

    public void sendMessage(CommandSender sender, MessageType type, String key, Object... params) {
        var raw = getMessage(type,key);
        raw = raw.replaceAll("\\$arg", "%s");
        sendRawMessage(sender, type, String.format(raw, params));
    }

    // END OF GENERALIZED FUNCTIONS

    public void sendQueueMessage(CommandSender sender, String key, Game game) {
        var name = game.getSettings().getName();
        sendMessage(sender, MessageType.STANDARD, "queue."+key, name);
    }





    public enum MessageType {
        ERROR("errors."),
        STANDARD("messages."),
        QUEUE("messages.queue.");
        private String value;

        MessageType(String value) {
            this.value = value;
        }

        String index() {
            return value;
        }
    }
}
