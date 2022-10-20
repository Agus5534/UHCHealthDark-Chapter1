package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.event.world.ChunkPopulateEvent;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(name = "&8More Ores",key = "more_ores",modalityType = ModalityType.MODE, material = Material.COAL_ORE)
public class MoreOresMode extends Modality {

    @Inject
    private Main plugin;

    public MoreOresMode() throws IllegalClassFormatException {
        super(ChatUtils.format("&7- Aumenta la Ore Gen"),
                ChatUtils.format("&4- UTILIZAR CON PRECAUCIÓN"));
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onChunkGenerate(ChunkPopulateEvent event) {
        if(!event.getWorld().getName().equals("uhc_world")) { return; }

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            for(Block b : chunkBlocks(event.getChunk())) {
                int n = ThreadLocalRandom.current().nextInt(1,300);

                //DIAMANTE
                if(b.getY() < 14) {
                    if(n < 4) {
                        if(b.getType() == Material.STONE) {
                            setOre(b, Material.DIAMOND_ORE,Material.STONE,4);
                        }

                        if(b.getType() == Material.DEEPSLATE) {
                            setOre(b,Material.DEEPSLATE_DIAMOND_ORE,Material.DEEPSLATE,4);
                        }
                    }
                }

                if(b.getY() < 22) {
                    //ORO
                    if(n < 5) {
                        if(b.getType() == Material.STONE) {
                            setOre(b, Material.GOLD_ORE,Material.STONE,3);
                        }
                    }
                }

                if(b.getY() < 37) {
                    //LAPIS
                    if(n < 7) {
                        if(b.getType() == Material.STONE) {
                            setOre(b, Material.LAPIS_ORE,Material.STONE,3);
                        }
                    }
                }
            }
        }, 1L);
    }


    private List<Block> chunkBlocks(Chunk chunk) {
        final int minX = 0;
        final int minZ = 0;
        final int maxX = 15;
        final int maxY = chunk.getWorld().getMaxHeight();
        final int maxZ = 15;

        List<Block> l = new ArrayList<>();

        for (int x = minX; x <= maxX; x++) {
            for (int y = 0; y <= maxY; y++) {
                for (int z = minZ; z <= maxZ; z++) {
                    l.add(chunk.getBlock(x,y,z));
                }
            }
        }

        return l;
    }

    private void setOre(Block b, Material ore, Material replace, int maxOre) {
        b.setType(ore);
     /*   var w = b.getWorld();

        int x = b.getX(), y = b.getY(), z = b.getZ();

        List<Block> bL = new ArrayList<>();

        bL.add(w.getBlockAt(x+1,ThreadLocalRandom.current().nextInt(y-1,y+1),z));
        bL.add(w.getBlockAt(x-1,ThreadLocalRandom.current().nextInt(y-1,y+1),z));
        bL.add(w.getBlockAt(x,ThreadLocalRandom.current().nextInt(y-1,y+1),z+1));
        bL.add(w.getBlockAt(x,ThreadLocalRandom.current().nextInt(y-1,y+1),z-1));
        bL.add(w.getBlockAt(x+1,ThreadLocalRandom.current().nextInt(y-1,y+1),z+1));
        bL.add(w.getBlockAt(x+1,ThreadLocalRandom.current().nextInt(y-1,y+1),z-1));
        bL.add(w.getBlockAt(x-1,ThreadLocalRandom.current().nextInt(y-1,y+1),z+1));
        bL.add(w.getBlockAt(x-1,ThreadLocalRandom.current().nextInt(y-1,y+1),z-1));

        int i = 1;
        for(Block bS : bL) {
            int n = ThreadLocalRandom.current().nextInt(1,50);

            if(bS.getType() != replace) { continue; }

            if(i >= maxOre) { continue; }

            if(n > 6) { continue; }

            bS.setType(ore);
            i++;
        }*/

    }
}
