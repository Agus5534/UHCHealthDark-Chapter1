package io.github.wickeddroidmx.plugin.utils.world;

import io.github.wickeddroidmx.plugin.utils.files.Configuration;
import org.bukkit.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;

import javax.inject.Inject;
import javax.inject.Named;
import java.io.File;
import java.io.IOException;
import java.util.BitSet;
import java.util.Random;

public class WorldGenerator {

    @Inject
    @Named("file-config")
    private Configuration configuration;

    private final String[] worlds = {
            "uhc_world",
            "world_nether",
            "world_the_end"
    };

    public void createWorld() {
        var list = configuration.getLongList("seed");

        WorldCreator worldCreator = new WorldCreator("uhc_world");
        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.type(WorldType.NORMAL);

        var uhcWorld = worldCreator.createWorld();

        if (list.size() > 0) {
            var seed = list.get(0);

            worldCreator.seed(seed);

            Bukkit.getLogger().info("Seed: " + seed);

            try {
                configuration.save();
            } catch (IOException e) {
                e.printStackTrace();
            }

        } else {
            Bukkit.getLogger().info("No hay seeds.");
        }

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
