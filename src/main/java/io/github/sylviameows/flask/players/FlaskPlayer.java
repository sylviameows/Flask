package io.github.sylviameows.flask.players;

import io.github.sylviameows.flask.game.Game;
import io.github.sylviameows.flask.game.Lobby;
import io.github.sylviameows.flask.managers.PlayerManager;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;

public class FlaskPlayer {
//    private final Player player; // should probably NOT do this.

    private Game game = null;
    private Lobby<?> lobby = null;

    public FlaskPlayer(Player player) {
        if (!PlayerManager.instance().has(player)) {
            PlayerManager.instance().add(player.getUniqueId().toString(), this);
        }

//        this.player = player;
    }

    public void setGame(Game game) {
        this.game = game;
    }
    public void setLobby(Lobby lobby) {
        this.lobby = lobby;
    }

    public Game getGame() {
        return game;
    }
    public Lobby<?> getLobby() {
        return lobby;
    }

    public void reset() {
        this.game = null;
        this.lobby = null;
    }

    public boolean isOccupied() {
        return game != null;
    }
    public boolean isInQueue() {
        return game != null && lobby == null;
    }
}
