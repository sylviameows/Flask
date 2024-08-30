package io.github.sylviameows.flask.services.world;

import com.infernalsuite.aswm.api.AdvancedSlimePaperAPI;
import com.infernalsuite.aswm.api.exceptions.CorruptedWorldException;
import com.infernalsuite.aswm.api.exceptions.NewerFormatException;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.utils.SchedulerUtil;
import org.bukkit.Bukkit;
import org.bukkit.event.world.WorldLoadEvent;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public interface WorldService {
    AdvancedSlimePaperAPI slime = AdvancedSlimePaperAPI.instance();
    Flask flask = Flask.getInstance();

    /**
     * Generates a temporary world that is an exact copy of the given "template" world. This world will not be saved.
     *
     * @param template the world to copy.
     * @param name     the name to give the clone.
     * @return the created clone.
     */
    default SlimeWorld useTemplate(SlimeWorld template, String name) {
        if (!template.isReadOnly()) {
            Flask.logger.warn("Loading template ("+template.getName()+") that is not read-only, it is recommended to only use read-only slime worlds.");
        }

        AtomicReference<SlimeWorld> world = new AtomicReference<>();
        SchedulerUtil.runSyncAndWait(flask, () -> {
            var clone = template.clone(name);
            world.set(loadWorld(clone));
        });
        return world.get();
    }

    /**
     * Generates a temporary world that is an exact copy of the given "template" world.
     * This new world is given a random UUID as its name. This world will not be saved.
     * @param template the world to copy.
     * @return the created clone.
     */
    default SlimeWorld useTemplate(SlimeWorld template) {
        return useTemplate(template, UUID.randomUUID().toString());
    }


    /**
     * Generates a temporary world that is an exact copy of the found "template" world. This world will not be saved.
     *
     * @param templateName the name of the world to copy.
     * @param cloneName    the name to give the clone.
     * @return a promise of the created clone.
     */
    default CompletableFuture<SlimeWorld> findAndUseTemplate(String templateName, String cloneName) {
        var promise = new CompletableFuture<SlimeWorld>();

        var templatePromise = readWorldAsync(templateName, true, new SlimePropertyMap()); // todo: better properties?
        templatePromise.whenComplete((template, exception) -> {
            if (exception != null || template == null) {
                throw new RuntimeException(exception);
            }
            promise.complete(useTemplate(template));
        });

        return promise;
    }

    /**
     * Generates a temporary world that is an exact copy of the found "template" world.
     * This new world is given a random UUID as its name. This world will not be saved.
     * @param templateName the name of the world to copy.
     * @return a promise of the created clone.
     */
    default CompletableFuture<SlimeWorld> findAndUseTemplate(String templateName) {
        return findAndUseTemplate(templateName, UUID.randomUUID().toString());
    }

    /**
     * Read a slime world into memory and deserialize it. This action is I/O and should be done asynchronously.
     * @param name the name of the world to read.
     * @param readOnly whether this world will be read-only.
     * @param properties the properties to apply to the world.
     * @return the deserialized slime world.
     * @throws CorruptedWorldException the world is corrupted.
     * @throws NewerFormatException the world was made for a newer version of SRF.
     * @throws UnknownWorldException the world doesn't exist.
     * @throws IOException the world is already being accessed by something else.
     */
    SlimeWorld readWorld(String name, boolean readOnly, SlimePropertyMap properties) throws CorruptedWorldException, NewerFormatException, UnknownWorldException, IOException;

    /**
     * Reads a slime world into memory and deserializes it asynchronously.
     * @param name the name of the world to read.
     * @param readOnly whether this world will be read-only.
     * @param properties the properties to apply to the world.
     * @return a promise of the deserialized slime world. may complete exceptionally.
     */
    default CompletableFuture<SlimeWorld> readWorldAsync(String name, boolean readOnly, SlimePropertyMap properties)  {
        var promise = new CompletableFuture<SlimeWorld>();
        var scheduler = Bukkit.getScheduler();

        // read the world.
        scheduler.runTaskAsynchronously(flask, task -> {
            try {
                var world = readWorld(name, readOnly, properties);
                promise.complete(world);
            } catch (Exception exception) {
                promise.completeExceptionally(exception);
            }
        });

        return promise;
    }

    /**
     * Loads a world into the server from memory. Must be done synchronously.
     * @param world the deserialized world to load, obtained from {@link WorldService#readWorld(String, boolean, SlimePropertyMap)}.
     * @param callEvent whether to trigger {@link WorldLoadEvent}.
     * @return a "mirror" or instance of the world that can now be interacted with.
     */
    default SlimeWorld loadWorld(SlimeWorld world, boolean callEvent) {
        return slime.loadWorld(world, callEvent);
    }
    /**
     * Loads a world into the server from memory. Must be done synchronously.
     * @param world the deserialized world to load, obtained from {@link WorldService#readWorld(String, boolean, SlimePropertyMap)}.
     * @return a "mirror" or instance of the world that can now be interacted with.
     */
    default SlimeWorld loadWorld(SlimeWorld world) {
        return loadWorld(world, true);
    }
}
