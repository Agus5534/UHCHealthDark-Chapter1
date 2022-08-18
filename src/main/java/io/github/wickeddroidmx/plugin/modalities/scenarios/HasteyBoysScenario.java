package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import team.unnamed.gui.core.item.type.ItemBuilder;

public class HasteyBoysScenario extends Modality {
    public HasteyBoysScenario() {
        super(ModalityType.SCENARIO, "hastey_boys", "&bHastey Boys", Material.DIAMOND_PICKAXE,
                ChatUtils.format("&7- Todas las herramientas tienen Eficiencia 3 e Irrompibilidad 1"));
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        var material = e.getRecipe().getResult().getType();

        if(isTool(material)){
            e.getInventory().setResult(ItemBuilder.newBuilder(material).addEnchant(Enchantment.DIG_SPEED, 3).addEnchant(Enchantment.DURABILITY, 1).build());
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        var current = e.getCurrentItem();

        if(current != null && isTool(current.getType()) && !(current.containsEnchantment(Enchantment.DIG_SPEED) || current.containsEnchantment(Enchantment.DURABILITY)) && !current.hasItemMeta()){
            e.setCurrentItem(ItemBuilder.newBuilder(current.getType()).addEnchant(Enchantment.DIG_SPEED, 3).addEnchant(Enchantment.DURABILITY, 1).build());
        }
    }

    private boolean isTool(Material material){
        return material.toString().endsWith("PICKAXE") ||
                material.toString().endsWith("SHOVEL") ||
                material.toString().endsWith("AXE") ||
                material.toString().endsWith("HOE") ||
                material == Material.SHEARS;
    }
}
