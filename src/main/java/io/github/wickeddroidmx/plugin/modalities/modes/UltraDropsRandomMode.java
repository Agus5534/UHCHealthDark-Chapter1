package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Bukkit;
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

    private final List<Material> possibleDrops = new ArrayList<>();

    List<String> errors = new ArrayList<>();
    public UltraDropsRandomMode() {
        super(ModalityType.MODE, "ultra_drops_random","&6Ultra Drops Random", Material.BEDROCK,
                ChatUtils.format("&7- Todos los drops serán aleatorios"));

         blockedMaterials = new Material[] {
                 Material.AIR,
                 Material.CAVE_AIR,
                 Material.VOID_AIR,
                 Material.END_CRYSTAL,
                 Material.END_GATEWAY,
                 Material.DEBUG_STICK,
                 Material.MOVING_PISTON,
                 Material.DEAD_TUBE_CORAL,
                 Material.DEAD_TUBE_CORAL_FAN,
                 Material.DEAD_TUBE_CORAL_WALL_FAN,
                 Material.WHITE_WALL_BANNER,
                 Material.ATTACHED_MELON_STEM,
                 Material.ATTACHED_PUMPKIN_STEM,
                 Material.BEETROOTS,
                 Material.LAVA_CAULDRON,
                 Material.LAVA,
                 Material.WATER,
                 Material.WATER_CAULDRON,
                 Material.POWDER_SNOW_CAULDRON,
                 Material.TRIPWIRE,
                 Material.POTATOES,
                 Material.BAMBOO_SAPLING,
                 Material.COCOA,
                 Material.PISTON_HEAD,
                 Material.REDSTONE_WIRE,
                 Material.FROSTED_ICE,
                 Material.CAVE_VINES,
                 Material.FIRE,
                 Material.BUBBLE_COLUMN,
                 Material.SOUL_FIRE,
                 Material.TALL_SEAGRASS,
                 Material.NETHER_PORTAL,
                 Material.END_PORTAL,
                 Material.CARROTS,
                 Material.POWDER_SNOW
         };

         for(Material m : Material.values()) {
             if(!m.isLegacy() && !m.isAir() && !Arrays.asList(blockedMaterials).contains(m) && !m.getKey().getKey().contains("wall") && !m.getKey().getKey().contains("potted") && !m.getKey().getKey().contains("candle_cake") && !m.getKey().getKey().contains("plant") && !m.getKey().getKey().contains("stem") && !m.getKey().getKey().contains("bush")) {
                 possibleDrops.add(m);
             }
         }
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

            Location loc = event.getBlock().getLocation();

            Material chosen = possibleDrops.get(new Random().nextInt(possibleDrops.size()));

            ItemStack item = new ItemCreator(chosen).canHaveEnchants();

            try {
                loc.getWorld().dropItem(loc, item);
            } catch (Exception e) {

                if(errors.contains("Material."+chosen.getKey().getKey().toUpperCase())){
                    return;
                }

                errors.add("Material."+chosen.getKey().getKey().toUpperCase());

                String s = "";

                for(String string : errors) {
                    s+= string + ", ";
                }

                Bukkit.getLogger().warning(s);
            }

        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent event) {
        ItemStack item = new ItemStack(possibleDrops.get(new Random().nextInt(possibleDrops.size())),1);

        if(!(event.getEntity() instanceof Player)) {
            if(!event.getDrops().isEmpty()) {
                event.getDrops().clear();

                event.getDrops().add(item);
            }
        }
    }
}
