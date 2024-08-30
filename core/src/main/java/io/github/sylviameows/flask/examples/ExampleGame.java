package io.github.sylviameows.flask.examples;

import io.github.sylviameows.flask.Palette;
import io.github.sylviameows.flask.api.FlaskPlugin;
import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.api.game.Lobby;
import io.github.sylviameows.flask.api.game.Phase;
import io.github.sylviameows.flask.api.game.Settings;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ExampleGame extends Game {
    private final List<Lobby<ExampleGame>> lobbies = new ArrayList<>();

    public ExampleGame(FlaskPlugin plugin) {
        super(plugin, Settings.of(
                "Sword Duel",
                "Fight against another player using only a sword.",
                Palette.MINT,
                Material.WOODEN_SWORD,
                2,
                2
        ));
    }

    @Override
    public Lobby<ExampleGame> createLobby(List<Player> players) {
        var lobby = new Lobby<>(this, players);
        lobbies.add(lobby);
        return lobby;
    }

    @Override
    public Phase initialPhase() {
        return new ExampleStartingPhase();
    }
}
