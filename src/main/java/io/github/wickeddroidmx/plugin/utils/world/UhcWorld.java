package io.github.wickeddroidmx.plugin.utils.world;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.utils.runnable.ListenerWatcher;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.craftbukkit.libs.org.apache.commons.io.FileUtils;
import org.bukkit.event.world.WorldUnloadEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class UhcWorld {

    private final String name;
    private World world;
    private long seed;
    private boolean recreatingWorld;
    private Main plugin;

    List<Biome> bannedBiomes;
    List<kaptainwutax.biomeutils.biome.Biome.Category> bannedCategories;

    public UhcWorld(long seed, Main plugin) {
        this.plugin = plugin;
        this.name = "uhc_world";
        this.seed = seed;
        this.world = null;
        this.bannedBiomes = new ArrayList<>();
        this.bannedCategories = new ArrayList<>();
        recreatingWorld = false;

        addBannedBiomes(
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
        );
    }


    private WorldCreator createUhcWorld() {
        WorldCreator worldCreator = new WorldCreator(this.name);

        if(seed != 0) {
            worldCreator.seed(this.seed);
        }

        worldCreator.environment(World.Environment.NORMAL);
        worldCreator.type(WorldType.NORMAL);

        return worldCreator;
    }

    public World createWorld() {
        if(plugin.getConfiguration().getBoolean("customworld")) {
            this.world = Bukkit.getWorld("world_minecraft_uhc_world");
            configWorld();
            return Bukkit.getWorld("world_minecraft_uhc_world");
        }
        var w = createUhcWorld().createWorld();

        if(this.seed == 0) {
            while(this.bannedBiomes.contains(getSpawnBiome(w))) {
                Bukkit.unloadWorld(w, false);

                w = createUhcWorld().createWorld();
            }
        }

        configWorld();

        this.recreatingWorld = false;
        this.world = w;
        return w;
    }

    private Biome getSpawnBiome(World w) {
        return w.getBiome(0,0,0);
    }

    public void recreateWorld(JavaPlugin plugin) {
        recreatingWorld = true;

        Bukkit.getOnlinePlayers().forEach(p -> {
            if(p.getWorld().equals(this.world)) {
                p.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }
        });


        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            Bukkit.unloadWorld(this.world, false);

            try {
                deleteWorldDir();
            } catch (Exception e) {}

            Bukkit.getScheduler().runTask(plugin, ()-> this.world = createWorld());
        }, 100L);

    }

    private void configWorld() {
        for (var world : Bukkit.getWorlds()) {
            world.setPVP(false);

            world.setDifficulty(Difficulty.HARD);
            if(world.getName().equals("world")) {
                world.setGameRule(GameRule.DO_FIRE_TICK, false);
                world.setPVP(true);
            }
            world.setGameRule(GameRule.NATURAL_REGENERATION, false);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, false);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, false);
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
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
    public List<kaptainwutax.biomeutils.biome.Biome.Category> getBannedCategories() { return bannedCategories; }

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

    public void addBannedCategory(kaptainwutax.biomeutils.biome.Biome.Category category) {
        this.bannedCategories.add(category);
    }

    public void removeBannedCategory(kaptainwutax.biomeutils.biome.Biome.Category category) {
        this.bannedCategories.remove(category);
    }

    public boolean isBannedCategory(kaptainwutax.biomeutils.biome.Biome.Category category) {
        return this.bannedCategories.contains(category);
    }

    public void removeBannedBiomes(Biome... b) {
        for(var biome : b) {
            this.bannedBiomes.remove(biome);
        }
    }

    public boolean isRecreatingWorld() {
        return recreatingWorld;
    }

    public void deleteWorldDirectory(String name) throws IOException {
        var file = new File(name);

        FileUtils.deleteDirectory(file);
    }

    public void deleteWorldDir() throws IOException {
        var file = new File(this.name);

        FileUtils.deleteDirectory(file);
    }
}
