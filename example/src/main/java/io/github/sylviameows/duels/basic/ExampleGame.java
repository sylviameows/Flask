package io.github.sylviameows.duels.basic;

import io.github.sylviameows.flask.api.FlaskPlugin;
import io.github.sylviameows.flask.api.game.*;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.List;

@GameProperties(
        name = "Sword Duel",
        description = "Fight against another player using only a sword.",
        color = 0x87ffdf, // mint color
        material = Material.WOODEN_SWORD,
        max = 2,
        min = 2
)
public class ExampleGame extends Game {
    public ExampleGame(FlaskPlugin plugin) {
        super(plugin);
    }

    @Override
    public Lobby<ExampleGame> createLobby(List<Player> players) {
        return new Lobby<>(this, players);
    }

    @Override
    public Phase initialPhase() {
        return new ExampleStartingPhase();
    }
}
