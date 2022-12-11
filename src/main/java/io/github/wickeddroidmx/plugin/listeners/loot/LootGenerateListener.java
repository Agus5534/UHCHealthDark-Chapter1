package io.github.wickeddroidmx.plugin.listeners.loot;

import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class LootGenerateListener implements Listener {

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        List<ItemStack> loot = new ArrayList<>();

        for(var i : event.getLoot()) {
            if(i.getType() == Material.SPECTRAL_ARROW) {
                loot.add(new ItemCreator(Material.ARROW).amount(
                        Math.min(i.getAmount() * 2, 64)
                ));
                continue;
            }

            loot.add(i);
        }

        event.setLoot(loot);
    }
}
