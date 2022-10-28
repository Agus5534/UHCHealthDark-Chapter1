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
    UhcWorld uhcWorld = new UhcWorld(0);

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

    public UhcWorld getUhcWorld() {
        return uhcWorld;
    }
}
