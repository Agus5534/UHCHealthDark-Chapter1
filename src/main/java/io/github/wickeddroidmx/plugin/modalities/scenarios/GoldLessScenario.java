package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryInteractEvent;
import org.bukkit.event.player.PlayerPickupItemEvent;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&gGold Less",
        modalityType = ModalityType.SCENARIO,
        key = "gold_less",
        experimental = true,
        material = Material.GOLD_BLOCK,
        lore = {"&7- El oro se encuentra desactivado"}
)
public class GoldLessScenario extends Modality {

    public GoldLessScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }

        if(event.getItem() == null) { return; }

        if(event.getItem().getItemStack() == null) { return; }

        var type = event.getItem().getItemStack().getType();

        if(type == Material.GOLD_BLOCK
        || type == Material.GOLD_INGOT
        || type == Material.RAW_GOLD
        || type == Material.RAW_GOLD_BLOCK
        || type == Material.DEEPSLATE_GOLD_ORE
        || type == Material.GOLD_ORE
        || type == Material.GOLD_NUGGET) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryUpdate(InventoryInteractEvent event) {
        if(!(event.getWhoClicked() instanceof Player)) { return; }

        var inv = event.getInventory();

        if(inv == null) { return; }

        try {
            inv.remove(Material.GOLD_BLOCK);
            inv.remove(Material.GOLD_INGOT);
            inv.remove(Material.RAW_GOLD);
            inv.remove(Material.RAW_GOLD_BLOCK);
            inv.remove(Material.DEEPSLATE_GOLD_ORE);
            inv.remove(Material.GOLD_ORE);
            inv.remove(Material.GOLD_NUGGET);
        } catch (Exception e) {
        }

    }
}
