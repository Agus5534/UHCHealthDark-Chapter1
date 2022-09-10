package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class UltraDropsRandomMode extends Modality {
    private final Material[] blockedMaterials;
    public UltraDropsRandomMode() {
        super(ModalityType.MODE, "ultra_drops_random","&6Ultra Drops Random", Material.BEDROCK,
                ChatUtils.format("&7- Todos los drops serán aleatorios"));

         blockedMaterials = new Material[] {
                 Material.AIR,
                 Material.CAVE_AIR,
                 Material.VOID_AIR,
                 Material.END_CRYSTAL,
                 Material.END_GATEWAY,
                 Material.DEBUG_STICK
        };
    }


    @EventHandler
    public void onDestroy(BlockBreakEvent event) {
        if(event.isDropItems()) {
            event.setDropItems(false);

            boolean drop = true;

            Material[] blockedTypes = new Material[]{
                    Material.TALL_GRASS,
                    Material.GRASS,
                    Material.SEAGRASS,
                    Material.ACACIA_LEAVES,
                    Material.AZALEA_LEAVES,
                    Material.BIRCH_LEAVES,
                    Material.OAK_LEAVES,
                    Material.DARK_OAK_LEAVES,
                    Material.FLOWERING_AZALEA_LEAVES,
                    Material.JUNGLE_LEAVES
            };


            if(event.getBlock().getType() != null) {

                if(Arrays.asList(blockedTypes).contains(event.getBlock().getType())) {
                    drop = 6 > ThreadLocalRandom.current().nextInt(1,100);
                }

            } else {
                return;
            }

            if(!drop) { return; }

            List<Material> possibleDrops = new ArrayList<>();

            for(Material m : Material.values()) {
                if(!m.isLegacy() || !m.isAir() || !Arrays.asList(blockedMaterials).contains(m)) {
                    possibleDrops.add(m);
                }
            }


            Location loc = event.getBlock().getLocation();

            ItemStack item = new ItemStack(possibleDrops.get(new Random().nextInt(possibleDrops.size())), 1);

            loc.getWorld().dropItem(loc, item);
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        List<Material> possibleDrops = new ArrayList<>();

        for(Material m : Material.values()) {
            if(!m.isLegacy() || !m.isAir() || !Arrays.asList(blockedMaterials).contains(m)) {
                possibleDrops.add(m);
            }
        }

        ItemStack item = new ItemStack(possibleDrops.get(new Random().nextInt(possibleDrops.size())),1);

        if(!(event.getEntity() instanceof Player)) {
            if(!event.getDrops().isEmpty()) {
                event.getDrops().clear();

                event.getDrops().add(item);
            }
        }
    }
}
