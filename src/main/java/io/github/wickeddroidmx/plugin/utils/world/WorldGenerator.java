package io.github.wickeddroidmx.plugin.utils.world;

import io.github.wickeddroidmx.plugin.Main;
import org.bukkit.Bukkit;

import java.io.File;
import java.io.IOException;

public class WorldGenerator {
    private final String[] worlds = {
            "uhc_world",
            "world_nether",
            "world_the_end",
            "world_minecraft_uhc_world"
    };

    UhcWorld uhcWorld;

    Main plugin;

    public WorldGenerator(Main plugin) {
        this.plugin = plugin;
        uhcWorld = new UhcWorld(0, plugin);
    }
    public void deleteWorlds() throws IOException {
        for (String worldName : worlds) {
            if(Bukkit.getWorld(worldName) != null) {
                Bukkit.unloadWorld(worldName, false);
                uhcWorld.deleteWorldDirectory(worldName);
            }
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


    private void setupWorldBorder() {
        var world = getUhcWorld().getWorld();

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
