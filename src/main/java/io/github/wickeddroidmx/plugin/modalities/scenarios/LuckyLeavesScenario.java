package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.instrument.IllegalClassFormatException;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        name = "&6Lucky Leaves",
        modalityType = ModalityType.SCENARIO,
        experimental = true,
        key = "lucky_leaves",
        lore = "&7- Los árboles pueden dar manzanas de oro",
        material = Material.GOLDEN_APPLE
)
public class LuckyLeavesScenario extends Modality {

    public LuckyLeavesScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent event) {
        if(event.getEntity() == null) { return; }

        if(event.getEntity().getType() != EntityType.DROPPED_ITEM) { return; }

        var item = ((Item)event.getEntity()).getItemStack();

        if(item == null) { return; }


        if(item.getType() == null) { return; }

        if(item.getType() != Material.APPLE) { return; }

        int n = ThreadLocalRandom.current().nextInt(1, 100);

        if(n >= 17 && n <= 21) {
            event.setCancelled(true);

            event.getLocation().getWorld().dropItem(event.getLocation(), new ItemCreator(Material.GOLDEN_APPLE));
        }
    }
}
