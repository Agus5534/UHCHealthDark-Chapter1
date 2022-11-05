package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        key = "guns_n_roses",
        name = "&cGuns N' Roses",
        material = Material.ROSE_BUSH,
        lore = {
                "&7- Al romper una poppy te dará una flecha.",
                "&7- Al romper una rose bush te dará 4 flechas y puede llegar a dar un arco."
        }
)
public class GunsNRosesScenario extends Modality {
    public GunsNRosesScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var block = e.getBlock();
        var type = block.getType();

        if (type == Material.POPPY) {
            e.setDropItems(false);

            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.ARROW));
        } else if (type == Material.ROSE_BUSH) {
            e.setDropItems(false);

            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.ARROW, 4));

            if (ThreadLocalRandom.current().nextInt(1,100) >= 90) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemCreator(Material.BOW).hasRandomEnchants());
            }
        }
    }
}
