package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.events.game.GameCutCleanEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&bDouble Ores",
        modalityType = ModalityType.SCENARIO,
        material = Material.NETHERITE_INGOT,
        key = "double_ores",
        experimental = true,
        lore = "&7- Los ores dropean el doble"
)
public class DoubleOresScenario extends Modality {

    @Inject
    private ModeManager modeManager;

    public DoubleOresScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        var type = event.getBlock().getType();


        if(isOre(type)) {
            if(modeManager.isActiveMode("cut_clean")) {
                Bukkit.getPluginManager().callEvent(new GameCutCleanEvent(event.getBlock(), event.getPlayer()));
                return;
            }

            switch (type) {
                case IRON_ORE, DEEPSLATE_IRON_ORE ->  event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.RAW_IRON));
                case GOLD_ORE, DEEPSLATE_GOLD_ORE ->  event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.RAW_GOLD));
                case COPPER_ORE,DEEPSLATE_COPPER_ORE ->  event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.RAW_COPPER));
                case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(Material.DIAMOND));
                default ->  event.getBlock().getWorld().dropItemNaturally(event.getBlock().getLocation(), new ItemStack(type));
            }

        }
    }


    private boolean isOre(Material material) {
       return material.toString().toUpperCase().endsWith("ORE") || material == Material.ANCIENT_DEBRIS;
    }
}
