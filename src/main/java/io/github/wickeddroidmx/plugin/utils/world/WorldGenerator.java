package io.github.wickeddroidmx.plugin.utils.world;

import io.github.wickeddroidmx.plugin.utils.files.Configuration;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.BitSet;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class WorldGenerator {

    @Inject
    @Named("file-config")
    private Configuration configuration;

    private final String[] worlds = {
            "uhc_world",
            "world_nether",
            "world_the_end"
    };

    World uhcWorld;

    private List<Biome> blockedBiomes;

    public void createWorld() {
        Biome[] ban = new Biome[] {
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
        };

        blockedBiomes = Arrays.asList(ban);

        long seed = randomSeed();

        uhcWorld = createUhcWorld(seed).createWorld();

        worldTasks();

        while (blockedBiomes.contains(getBiome())) {
            Bukkit.unloadWorld(uhcWorld, false);
            seed = randomSeed();
            uhcWorld = createUhcWorld(seed).createWorld();
            worldTasks();

            Bukkit.getLogger().info(String.format("Changing world"));
        }

    }

    private long randomSeed() {
        return ThreadLocalRandom.current().nextLong(-Long.MAX_VALUE,Long.MAX_VALUE);
    }

    private Biome getBiome() {
        return uhcWorld.getBiome(0,0,0);
    }

    public WorldCreator createUhcWorld(long seed) {
        WorldCreator worldCreator = new WorldCreator("uhc_world");
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.type(WorldType.NORMAL);

        return worldCreator;
    }

    public void worldTasks() {
        if (uhcWorld != null) {
            uhcWorld.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            uhcWorld.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            uhcWorld.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        }

        for (var world : Bukkit.getWorlds()) {
            world.setPVP(false);

            world.setDifficulty(Difficulty.HARD);
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
        }
    }

    public void deleteWorlds() throws IOException {
        for (String worldName : worlds) {
            Bukkit.unloadWorld(worldName, false);
            deleteWorldDirectory(worldName);
        }

        deleteDirectory("advancements");
        deleteDirectory("playerdata");
        deleteDirectory("stats");
    }

    private void deleteDirectory(String name) throws IOException {
        var world = Bukkit.getWorld("world");

        if (world != null) {
            var files = new File(world.getWorldFolder().getAbsolutePath() + "/" + name + "/").listFiles();

            if (files != null) {
                for (var file : files) {
                    file.delete();
                }
            }
        }
    }



    private void deleteWorldDirectory(String name) throws IOException {
        var file = new File(name);

        FileUtils.deleteDirectory(file);
    }

    private void setupWorldBorder() {
        var world = Bukkit.getWorld("uhc_world");

        if (world != null) {
            var worldBorder= world.getWorldBorder();

            worldBorder.setSize(4000);
            worldBorder.setDamageAmount(0);
        }
    }
}
