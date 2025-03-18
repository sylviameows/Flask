package io.github.sylviameows.flask.services;

import com.infernalsuite.asp.api.exceptions.CorruptedWorldException;
import com.infernalsuite.asp.api.exceptions.NewerFormatException;
import com.infernalsuite.asp.api.exceptions.UnknownWorldException;
import com.infernalsuite.asp.api.loaders.SlimeLoader;
import com.infernalsuite.asp.api.world.SlimeWorld;
import com.infernalsuite.asp.api.world.properties.SlimePropertyMap;
import com.infernalsuite.asp.loaders.file.FileLoader;
import io.github.sylviameows.flask.Flask;
import io.github.sylviameows.flask.api.services.WorldService;
import io.github.sylviameows.flask.api.util.SchedulerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.io.File;
import java.io.IOException;

public class FileWorldService implements WorldService {
    private final SlimeLoader loader;

    private final String ROOT_DIRECTORY = Flask.getInstance().getPlugin().getDataPath()+"/.maps";

    public FileWorldService() {
        var directory = new File(ROOT_DIRECTORY);
        loader = new FileLoader(directory);
    }

    @Override
    public SlimeWorld readWorld(String name, boolean readOnly, SlimePropertyMap properties) throws CorruptedWorldException, NewerFormatException, UnknownWorldException, IOException {
        return slime.readWorld(loader, name, readOnly, properties);
    }

    @Override
    public SlimeWorld createWorld(String name, boolean readOnly, SlimePropertyMap properties) throws IOException {
        var world = slime.createEmptyWorld(name, readOnly, properties, loader);

        SchedulerUtil.runSyncAndWait(flask.getPlugin(), () -> {
            loadWorld(world, true);

            Location location = new Location(Bukkit.getWorld(name), 0, 61, 0);
            location.getBlock().setType(Material.BEDROCK);
        });


        new File(ROOT_DIRECTORY, name).getParentFile().mkdirs();

        slime.saveWorld(world);

        return world;
    }
}
