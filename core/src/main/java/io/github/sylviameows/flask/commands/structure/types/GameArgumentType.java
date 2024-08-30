package io.github.sylviameows.flask.commands.structure.types;

import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.registries.GameRegistryImpl;
import io.papermc.paper.command.brigadier.argument.ArgumentTypes;
import io.papermc.paper.command.brigadier.argument.CustomArgumentType;
import org.bukkit.NamespacedKey;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GameArgumentType implements CustomArgumentType<Game, NamespacedKey> {
    private final GameRegistryImpl registry = GameRegistryImpl.instance();

    public static GameArgumentType game() {
        return new GameArgumentType();
    }


    @Override
    public @NotNull Game parse(@NotNull StringReader reader) throws CommandSyntaxException {
        final int start = reader.getCursor();

        while (reader.canRead() && (StringReader.isAllowedInUnquotedString(reader.peek()) || reader.peek() == ':')) {
            reader.skip();
        }
        String result = reader.getString().substring(start, reader.getCursor());

        Game game;
        if (result.contains(":")) {
            NamespacedKey key = NamespacedKey.fromString(result);
            game = registry.get(key);
        } else {
            game = registry.findByName(result);
        }

        if (game == null) {
            var message = new LiteralMessage("Invalid game argument.");
            throw new SimpleCommandExceptionType(message).createWithContext(reader);
        }

        return game;
    }

    @Override
    public @NotNull ArgumentType<NamespacedKey> getNativeType() {
        return ArgumentTypes.namespacedKey();
    }

    @Override
    public @NotNull Collection<String> getExamples() {
        return Collections.emptyList();
    }

    @Override
    public @NotNull <S> CompletableFuture<Suggestions> listSuggestions(@NotNull CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        var keys = registry.keys();
        var input = builder.getRemainingLowerCase();

        List<String> namespaces = new ArrayList<>();

        for (NamespacedKey key : keys) {
            Game game = registry.get(key);
            Message message = new LiteralMessage(game.getSettings().getName());

            if (!input.contains(":")) {
                String value = key.value();
                if (value.startsWith(input)) {
                    builder.suggest(value, message);
                }

                String namespace = key.namespace();
                if (!namespaces.contains(namespace)) {
                    builder.suggest(namespace+":");
                    namespaces.add(namespace);
                }
            } else {
                String string = key.asString();
                if (string.startsWith(input)) builder.suggest(string, message);
            }
        }

        return builder.buildFuture();
    }
}
