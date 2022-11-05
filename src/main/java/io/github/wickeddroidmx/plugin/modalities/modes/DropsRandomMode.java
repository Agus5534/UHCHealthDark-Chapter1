package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.instrument.IllegalClassFormatException;
import java.util.*;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "drops_random",
        name = "&6Drops Random",
        material = Material.DRAGON_EGG,
        lore = {"&7- Los bloques tienen diferentes dropeos"}
)
public class DropsRandomMode extends Modality {

    private HashMap<Material, Material> blockDrops = new HashMap<>();

    private final Material[] blockedMaterials;

    private final List<Material> possibleDrops = new ArrayList<>();

    public DropsRandomMode() throws IllegalClassFormatException {
        super();

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
                Material.ATTACHED_MELON_STEM,
                Material.ATTACHED_PUMPKIN_STEM,
                Material.BEETROOTS,
                Material.LAVA_CAULDRON,
                Material.POWDER_SNOW_CAULDRON,
                Material.WATER_CAULDRON,
                Material.LAVA,
                Material.WATER,
                Material.TRIPWIRE,
                Material.POTATOES,
                Material.BAMBOO_SAPLING,
                Material.COCOA,
                Material.PISTON_HEAD,
                Material.REDSTONE_WIRE,
                Material.FROSTED_ICE,
                Material.CAVE_VINES
        };

        for(Material m : Material.values()) {
            if(!m.isLegacy() && !m.isAir() && !Arrays.asList(blockedMaterials).contains(m) && !m.getKey().getKey().contains("wall") && !m.getKey().getKey().contains("potted") && !m.getKey().getKey().contains("candle_cake") && !m.getKey().getKey().contains("plant") && !m.getKey().getKey().contains("stem") && !m.getKey().getKey().contains("bush")) {
                possibleDrops.add(m);
            }
        }
    }

    @EventHandler
    public void onDestroy(BlockBreakEvent event) {
        if(!event.isDropItems()) { return; }

        event.setDropItems(false);

        addDrop(event.getBlock().getType());

        Location loc = event.getBlock().getLocation();

        ItemStack item = new ItemStack(blockDrops.get(event.getBlock().getType()),1);

        loc.getWorld().dropItem(loc, item);
    }

    private void addDrop(Material material) {
        if(blockDrops.containsKey(material)) { return; }

        Material m = possibleDrops.get(new Random().nextInt(possibleDrops.size()));

        blockDrops.put(material,m);

        possibleDrops.remove(m);
    }
}
