package io.github.sylviameows.flask.services.world;

import com.infernalsuite.aswm.api.world.properties.SlimePropertyMap;

import static com.infernalsuite.aswm.api.world.properties.SlimeProperties.*;

public class WorldProperties {
    double[] coords = new double[]{0.5,64,0.5,0,0}; // x,y,z,pitch,yaw
    boolean pvp = true;

    String difficulty = "normal";
    boolean allowMonsters = false;
    boolean allowAnimals = false;
    boolean dragonBattle = false;

    String environment = "NORMAL";
    String worldType = "DEFAULT";
    String defaultBiome = "minecraft:plains";

    // todo: edit property functions

    public static SlimePropertyMap defaultProperties() {
        return new WorldProperties().build();
    }

    public SlimePropertyMap build() {
        var map = new SlimePropertyMap();

        map.setValue(SPAWN_X, (int) coords[0]);
        map.setValue(SPAWN_Y, (int) coords[1]);
        map.setValue(SPAWN_Z, (int) coords[2]);
        map.setValue(SPAWN_YAW, (float) coords[4]);

        map.setValue(PVP, pvp);
        map.setValue(DIFFICULTY, difficulty);
        map.setValue(ALLOW_ANIMALS, allowAnimals);
        map.setValue(ALLOW_MONSTERS, allowMonsters);
        map.setValue(DRAGON_BATTLE, dragonBattle);
        map.setValue(ENVIRONMENT, environment);
        map.setValue(WORLD_TYPE, worldType);
        map.setValue(DEFAULT_BIOME, defaultBiome);

        // todo: max size properties?

        return map;
    }

    public void setSpawnX(double x) {
        coords[0] = x;
    }
    public void setSpawnY(double y) {
        coords[1] = y;
    }
    public void setSpawnZ(double z) {
        coords[2] = z;
    }
    public void setSpawnPitch(double pitch) {
        coords[3] = pitch;
    }
    public void setSpawnYaw(double yaw) {
        coords[4] = yaw;
    }
    public double getSpawnX() {
        return coords[0];
    }
    public double getSpawnY() {
        return coords[1];
    }
    public double getSpawnZ() {
        return coords[2];
    }
    public double getSpawnPitch() {
        return coords[3];
    }
    public double getSpawnYaw() {
        return coords[4];
    }
    public boolean isPvp() {
        return pvp;
    }
    public void setPvp(boolean pvp) {
        this.pvp = pvp;
    }
    public String getDifficulty() {
        return difficulty;
    }
    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }
    public boolean isAllowMonsters() {
        return allowMonsters;
    }
    public void setAllowMonsters(boolean allowMonsters) {
        this.allowMonsters = allowMonsters;
    }
    public boolean isAllowAnimals() {
        return allowAnimals;
    }
    public void setAllowAnimals(boolean allowAnimals) {
        this.allowAnimals = allowAnimals;
    }
    public boolean isDragonBattle() {
        return dragonBattle;
    }
    public void setDragonBattle(boolean dragonBattle) {
        this.dragonBattle = dragonBattle;
    }
    public String getEnvironment() {
        return environment;
    }
    public void setEnvironment(String environment) {
        this.environment = environment;
    }
    public String getWorldType() {
        return worldType;
    }
    public void setWorldType(String worldType) {
        this.worldType = worldType;
    }
    public String getDefaultBiome() {
        return defaultBiome;
    }
    public void setDefaultBiome(String defaultBiome) {
        this.defaultBiome = defaultBiome;
    }
}
