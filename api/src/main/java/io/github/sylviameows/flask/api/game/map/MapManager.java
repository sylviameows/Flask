package io.github.sylviameows.flask.api.game.map;

import com.infernalsuite.aswm.api.exceptions.CorruptedWorldException;
import com.infernalsuite.aswm.api.exceptions.NewerFormatException;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import io.github.sylviameows.flask.api.FlaskAPI;
import io.github.sylviameows.flask.api.game.Game;
import io.github.sylviameows.flask.api.manager.Manager;
import io.github.sylviameows.flask.api.services.WorldService;
import io.github.sylviameows.flask.api.util.SchedulerUtil;
import io.github.sylviameows.flask.api.util.WorldProperties;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class MapManager<T extends GameMap> implements Manager<T> {
    private final WorldService ws = FlaskAPI.instance().getWorldService();

    private final Map<String, T> maps;
    private final String prefix;

    public MapManager(Game<T> game, Class<T> clazz) {
        this(game.getIdentifier().namespace()+"/"+game.getIdentifier().value()+"/", clazz);
    }

    protected MapManager(String prefix, Class<T> clazz) {
        this.prefix = prefix;
        this.maps = new HashMap<>();
    }

    private MapManager(String prefix, Map<String, T> map) {
        this.prefix = prefix;
        this.maps = map;
    }

    protected void initialize(Class<T> clazz) {
        // clazz.getFields()
    }

    public CompletableFuture<SlimeWorld> getWorld(String id, boolean read_only) {
        var promise = new CompletableFuture<SlimeWorld>();

        Bukkit.getScheduler().runTaskAsynchronously(FlaskAPI.instance().getPlugin(), () -> {
            try {
                var world = ws.readWorld(prefix+id, read_only, WorldProperties.defaultProperties());

                SchedulerUtil.runSyncAndWait(FlaskAPI.instance().getPlugin(), () -> promise.complete(ws.loadWorld(world)));
            } catch (CorruptedWorldException | IOException | NewerFormatException e) {
                promise.completeExceptionally(e);
                throw new RuntimeException(e);
            } catch (UnknownWorldException e) {
                ws.createWorldAsync(prefix+id).whenComplete((world, ex) -> {
                    if (ex != null || world == null) {
                        promise.completeExceptionally(ex);
                    }
                    promise.complete(world);
                });
            }

        });

        return promise;
    }

    public CompletableFuture<SlimeWorld> getWorld(String id) {
        return getWorld(id, false);
    }

    public CompletableFuture<SlimeWorld> getWorld(T map) {
        return getWorld(map.getId());
    }

    public CompletableFuture<SlimeWorld> getWorld(T map, boolean read_only) {
        return getWorld(map.getId(), read_only);
    }

    public T add(T map) {
        return add(map.getId(), map);
    }

    public T add(String key, T entry) {
        return maps.put(key, entry);
    }

    public T get(String key) {
        return maps.get(key);
    }

    public T remove(T map) {
        return remove(map.getId());
    }

    public T remove(String key) {
        return maps.remove(key);
    }

    public Set<String> keys() {
        return maps.keySet();
    }

    public Collection<T> values() {
        return maps.values();
    }

    protected Map<String, T> getMaps() {
        return this.maps;
    }

    protected String getPrefix() {
        return this.prefix;
    }
}
