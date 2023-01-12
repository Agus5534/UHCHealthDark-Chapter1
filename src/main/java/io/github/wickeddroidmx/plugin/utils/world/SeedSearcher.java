package io.github.wickeddroidmx.plugin.utils.world;

import io.github.wickeddroidmx.plugin.Main;
import kaptainwutax.biomeutils.source.OverworldBiomeSource;
import kaptainwutax.mcutils.util.pos.BPos;
import kaptainwutax.mcutils.version.MCVersion;
import org.bukkit.Bukkit;
import org.bukkit.block.Biome;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class SeedSearcher {
    private final Main plugin;
    private int trials;
    private List<Biome> excludedBiomes;
    private List<Long> availableSeeds;

    public Runnable onTaskEnd;
    public Runnable onNewSearch;
    int searched, searchRadius;
    public int taskIDTwo;
    List<kaptainwutax.biomeutils.biome.Biome.Category> excludedCategories;

    public SeedSearcher(Main plugin, int trials, List<Biome> excludedBiomes, int searchRadius, List<kaptainwutax.biomeutils.biome.Biome.Category> excludedCategories) {
        this.plugin = plugin;
        this.trials = trials;
        this.excludedBiomes = excludedBiomes;
        searched = 0;
        this.searchRadius = searchRadius;
        this.excludedCategories = excludedCategories;

        availableSeeds = new ArrayList<>();
    }

    public void startTask() {
        var task = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, this::search, 2L, 4L);

        taskIDTwo = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
            if(searched >= trials) {
                Bukkit.getScheduler().cancelTask(task);
                onTaskEnd.run();
            }
        }, 1L, 1L);
    }

    private void search() {
        long seed = ThreadLocalRandom.current().nextLong(-99999999999999999L, 99999999999999999L);
        List<Boolean> result = new ArrayList<>();

        searched++;
        onNewSearch.run();

        var biomeSource = new OverworldBiomeSource(MCVersion.v1_17_1, seed);

        for(int x = 0 ; x < (int)(searchRadius/16) ; x++) {
            for(int z = 0; z < (int)(searchRadius/16) ; z++) {
                result.add(this.isCompatible(biomeSource.getBiome(x, 0, z)));
                result.add(this.isCompatible(biomeSource.getBiome(-x, 0, z)));
                result.add(this.isCompatible(biomeSource.getBiome(x, 0, -z)));
                result.add(this.isCompatible(biomeSource.getBiome(-x, 0, -z)));
            }
        }

        if(!result.contains(false)) {
            availableSeeds.add(seed);
        }
    }

    private boolean isCompatible(kaptainwutax.biomeutils.biome.Biome biome) {
        List<String> biomeNames = new ArrayList<>();
        boolean compatible = true;

        for(var b : excludedBiomes) {
            biomeNames.add(b.toString().toLowerCase());
        }

        if(biomeNames.contains(biome.getName())) {
            compatible = false;
        }

        if(excludedCategories.contains(biome.getCategory())) {
            return false;
        }

        return compatible;
    }

    public int getSearched() {
        return searched;
    }

    public List<Long> getAvailableSeeds() {
        return availableSeeds;
    }
}
