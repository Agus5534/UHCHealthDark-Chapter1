package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class CobwebLess extends Modality {
    public CobwebLess() {
        super(ModalityType.SCENARIO, "cobweb_less", "&7Cobweb less", Material.COBWEB,
                ChatUtils.format("&7No puedes usar cobwebs."));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var block = e.getBlock();

        if (block.getType() == Material.COBWEB) {
            e.setDropItems(false);

            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.STRING));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e) {
        var block = e.getBlock();

        if (block.getType() == Material.COBWEB) {
            e.setCancelled(true);
        }
    }
}
