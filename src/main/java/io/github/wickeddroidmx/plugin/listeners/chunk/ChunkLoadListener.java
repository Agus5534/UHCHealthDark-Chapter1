package io.github.wickeddroidmx.plugin.listeners.chunk;

import org.bukkit.Bukkit;
import org.bukkit.block.Biome;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.ChunkLoadEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ChunkLoadListener implements Listener {

    private List<Biome> blockedBiomes;

    private Biome newBiome;

    public void chunkListener() {
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
    }

    @EventHandler
    public void onChunkLoad(ChunkLoadEvent event) {
        chunkListener();


        if(event.getChunk().getX() >= 4) { return; }
        if(!event.getWorld().getName().equals("uhc_world")) { return; }
        if(!event.isNewChunk()) { return; }


        for(int x = 0; x < 16; x++) {
            for(int z = 0; z < 16; z++) {
                var block = event.getChunk().getBlock(x,4,z); //Funciona en WorldGen pre 1.18
                var biome = block.getBiome();

                if(!blockedBiomes.contains(biome)) {
                    return;
                }

                String originalBiome = block.getBiome().getKey().getKey();

               /* while (blockedBiomes.contains(biome) && event.getChunk().getX() < 4) {
                    biome = block.getBiome();

                    if(newBiome == null) {
                        newBiome = Arrays.asList(Biome.values()).get(new Random().nextInt(30));
                    }


                    block.setBiome(newBiome);

                    event.getWorld().refreshChunk(event.getChunk().getX(), event.getChunk().getZ());

                    Bukkit.getLogger().info(
                            String.format("Chunk %s %s fixed w/biome %s | Old Biome: %s",
                                    event.getChunk().getX(),
                                    event.getChunk().getZ(),
                                    newBiome.getKey().getKey(),
                                    originalBiome)
                    );
                }*/
            }
        }
    }
}
