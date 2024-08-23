package io.github.sylviameows.flask.examples;

import io.github.sylviameows.flask.Palette;
import io.github.sylviameows.flask.game.Game;
import io.github.sylviameows.flask.game.Lobby;
import io.github.sylviameows.flask.game.Phase;
import io.github.sylviameows.flask.game.Settings;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;

public class ExampleGame extends Game {
    public ExampleGame(Plugin plugin) {
        super(plugin, Settings.of(
                "Example",
                "Change this example to fit <bold>your</bold> game's needs!",
                Palette.MINT,
                Material.ENDER_EYE,
                2,
                2
        ));
    }

    @Override
    public Lobby<ExampleGame> createLobby() {
        return new Lobby<>(this);
    }

    @Override
    public Phase initialPhase() {
        return new ExamplePhase();
    }
}
