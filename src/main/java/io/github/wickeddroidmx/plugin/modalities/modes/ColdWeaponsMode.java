package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.event.world.LootGenerateEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

@GameModality(
        name = "&bCold Weapons",
        material = Material.BLUE_ICE,
        key = "cold_weapons",
        modalityType = ModalityType.MODE,
        lore = {"&7- Flame y FireAspect se encuentran desactivados"}
)
public class ColdWeaponsMode extends Modality {

    public ColdWeaponsMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if(event.getEnchantsToAdd().containsKey(Enchantment.FIRE_ASPECT) || event.getEnchantsToAdd().containsKey(Enchantment.ARROW_FIRE)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEnchantAnvil(PrepareAnvilEvent event) {
        if(event.getResult() == null) { return; }

        if(event.getResult().containsEnchantment(Enchantment.ARROW_FIRE) || event.getResult().containsEnchantment(Enchantment.FIRE_ASPECT)) {
            var ench = (event.getResult().containsEnchantment(Enchantment.FIRE_ASPECT) ? Enchantment.FIRE_ASPECT : Enchantment.ARROW_FIRE);

            var result = event.getResult();
            var resultMeta = result.getItemMeta();

            var itemStack2 = result.clone();
            resultMeta.removeEnchant(ench);

            itemStack2.setItemMeta(resultMeta);
            event.setResult(itemStack2);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        var current = event.getCurrentItem();

        if(current == null) { return; }

        if(current.containsEnchantment(Enchantment.ARROW_FIRE)) {
            current.removeEnchantment(Enchantment.ARROW_FIRE);
        }

        if(current.containsEnchantment(Enchantment.FIRE_ASPECT)) {
            current.removeEnchantment(Enchantment.FIRE_ASPECT);
        }

    }

    @EventHandler
    public void onLootGenerate(LootGenerateEvent event) {
        Collection<ItemStack> newLoot = new ArrayList<>();

        for(var i : event.getLoot()) {
            if(i.containsEnchantment(Enchantment.FIRE_ASPECT)) { i.removeEnchantment(Enchantment.FIRE_ASPECT); }
            if(i.containsEnchantment(Enchantment.ARROW_FIRE)) { i.removeEnchantment(Enchantment.ARROW_FIRE); }

            newLoot.add(i);
        }

        event.setLoot(newLoot);
    }
}
