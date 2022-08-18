package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class TimberScenario extends Modality {

    @Inject
    private Main plugin;

    public TimberScenario() {
        super(ModalityType.SCENARIO, "timber", "&aTimber", Material.OAK_LOG,
                ChatUtils.format("&7- Los arboles se talan de una."));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var block = e.getBlock();

        if (isLog(block.getType())) {
            breakTree(block);
        }
    }

    private void breakTree(Block block) {
        if (isLog(block.getType())) {
            block.breakNaturally(new ItemStack(Material.AIR), true);

            for (var face : BlockFace.values()){
                if (face.equals(BlockFace.UP))
                    Bukkit.getScheduler().runTaskLater(plugin, () -> breakTree(block.getRelative(face)), 3L);
            }
        }
    }

    private boolean isLog(Material material) {
        return material.toString().toLowerCase().endsWith("_log");
    }
}
