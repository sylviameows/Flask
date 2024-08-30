package io.github.sylviameows.flask.services.world;

import com.infernalsuite.aswm.api.exceptions.CorruptedWorldException;
import com.infernalsuite.aswm.api.exceptions.NewerFormatException;
import com.infernalsuite.aswm.api.exceptions.UnknownWorldException;
import com.infernalsuite.aswm.api.loaders.SlimeLoader;
import com.infernalsuite.aswm.api.world.SlimeWorld;
import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;
import com.infernalsuite.aswm.loaders.file.FileLoader;

import java.io.File;
import java.io.IOException;

public class FileWorldService implements WorldService {
    private final SlimeLoader loader;

    public FileWorldService() {
        var directory = new File("flask_worlds");
        loader = new FileLoader(directory);
    }

    @Override
    public SlimeWorld readWorld(String name, boolean readOnly, SlimePropertyMap properties) throws CorruptedWorldException, NewerFormatException, UnknownWorldException, IOException {
        return slime.readWorld(loader, name, readOnly, properties);
    }
}
