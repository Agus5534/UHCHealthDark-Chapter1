package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class DropsRandomMode extends Modality {

    private HashMap<Material, Material> blockDrops = new HashMap<>();

    private final Material[] blockedMaterials;

    private final List<Material> possibleDrops = new ArrayList<>();

    public DropsRandomMode() {
        super(ModalityType.MODE, "drops_random", "&6Drops Random", Material.STRUCTURE_VOID,
                ChatUtils.format("&7- Los bloques tienen diferentes dropeos"));

        blockedMaterials = new Material[] {
                Material.AIR,
                Material.CAVE_AIR,
                Material.VOID_AIR,
                Material.END_CRYSTAL,
                Material.END_GATEWAY,
                Material.DEBUG_STICK
        };

        for(Material m : Material.values()) {
            if(!m.isLegacy() || !m.isAir() || !Arrays.asList(blockedMaterials).contains(m)) {
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
