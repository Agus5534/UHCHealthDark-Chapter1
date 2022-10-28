package io.github.wickeddroidmx.plugin.utils.world;

import org.bukkit.*;
import org.bukkit.block.Biome;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class UhcWorld {

    private final String name;
    private World world;
    private long seed;
    private boolean recreatingWorld;

    List<Biome> bannedBiomes = Arrays.asList(new Biome[] {
            Biome.OCEAN,
            Biome.COLD_OCEAN,
            Biome.DEEP_COLD_OCEAN,
            Biome.DEEP_OCEAN,
            Biome.DEEP_WARM_OCEAN,
            Biome.FROZEN_OCEAN,
            Biome.DEEP_FROZEN_OCEAN,
            Biome.WARM_OCEAN,
            Biome.LUKEWARM_OCEAN,
            Biome.DEEP_LUKEWARM_OCEAN,
            Biome.BAMBOO_JUNGLE_HILLS
    });

    public UhcWorld(long seed) {
        this.name = "uhc_world";
        this.seed = seed;
        this.world = null;
        recreatingWorld = false;
    }


    public WorldCreator createUhcWorld() {
        WorldCreator worldCreator = new WorldCreator(this.name);

        if(seed != 0) {
            worldCreator.seed(this.seed);
        }

        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.type(WorldType.NORMAL);

        return worldCreator;
    }

    private World createWorld() {

        var w = createUhcWorld().createWorld();

        if(this.seed == 0) {
            while(this.bannedBiomes.contains(getSpawnBiome(w))) {
                Bukkit.unloadWorld(w, false);

                w = createUhcWorld().createWorld();
            }
        }

        configWorld();

        this.recreatingWorld = false;
        return w;
    }

    private Biome getSpawnBiome(World w) {
        return w.getBiome(0,0,0);
    }

    public void recreateWorld() {
        recreatingWorld = true;

        Bukkit.getOnlinePlayers().forEach(p -> {
            if(p.getWorld().equals(this.world)) {
                p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
        });

        this.world = createWorld();
    }

    private void configWorld() {
        if (world != null) {
                world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
                world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
                world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        }

        for (var world : Bukkit.getWorlds()) {
            world.setPVP(false);

            world.setDifficulty(Difficulty.HARD);
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        }
    }


    public World getWorld() {
        return world;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public List<Biome> getBannedBiomes() {
        return bannedBiomes;
    }

    public boolean isBannedBiome(Biome b) {
        return this.bannedBiomes.contains(b);
    }

    public void setBannedBiomes(List<Biome> bannedBiomes) {
        this.bannedBiomes = bannedBiomes;
    }

    public void addBannedBiomes(Biome... b) {
        for(var biome : b) {
            this.bannedBiomes.add(biome);
        }
    }

    public boolean isRecreatingWorld() {
        return recreatingWorld;
    }
}
