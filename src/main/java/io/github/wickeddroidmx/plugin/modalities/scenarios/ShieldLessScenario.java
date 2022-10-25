package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryPickupItemEvent;
import org.bukkit.inventory.Recipe;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&6Shield Less",
        modalityType = ModalityType.SCENARIO,
        key = "shield_less",
        material = Material.SHIELD
)
public class ShieldLessScenario extends Modality {

    public ShieldLessScenario() throws IllegalClassFormatException {
        super(ChatUtils.format("&7- Los escudos se encuentran desactivados"));
    }

    @EventHandler
    public void onCraft(CraftItemEvent event) {
        if(event.getRecipe().getResult().getType() == Material.SHIELD) {
            event.setResult(Event.Result.DENY);
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        var current = event.getCurrentItem();

        if(current == null) { return; }

        if(current.getType() == Material.SHIELD) {
            event.getWhoClicked().getInventory().remove(Material.SHIELD);
        }
    }

    @EventHandler
    public void onPickupItem(InventoryPickupItemEvent event) {
        if(event.getItem().getItemStack() == null) { return; }


        if(event.getItem().getItemStack().getType() == Material.SHIELD) {
            event.setCancelled(true);
        }
    }
}
