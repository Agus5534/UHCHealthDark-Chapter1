package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Random;

public class GunsNRosesScenario extends Modality {
    public GunsNRosesScenario() {
        super(ModalityType.SCENARIO, "guns_n_roses", "&cGuns N' Roses", Material.POPPY,
                ChatUtils.format("&7- Al romper una poppy te dará una flecha"),
                ChatUtils.format("&7- Al romper una rose bush te dará 4 flechas y puede llegar a dar un arco"));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var block = e.getBlock();
        var type = block.getType();
        var random = new Random();

        if (type == Material.POPPY) {
            e.setDropItems(false);

            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.ARROW));
        } else if (type == Material.ROSE_BUSH) {
            e.setDropItems(false);

            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.ARROW, 4));

            if (random.nextInt(100) >= 90) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.BOW));
            }
        }
    }
}
