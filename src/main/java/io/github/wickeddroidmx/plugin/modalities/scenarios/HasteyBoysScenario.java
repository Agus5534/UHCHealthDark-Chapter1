package io.github.wickeddroidmx.plugin.modalities.scenarios;

import com.destroystokyo.paper.event.inventory.PrepareGrindstoneEvent;
import com.destroystokyo.paper.event.inventory.PrepareResultEvent;
import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import io.github.wickeddroidmx.plugin.utils.items.ItemPersistentData;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.CraftItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.persistence.PersistentDataType;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;

public class HasteyBoysScenario extends Modality {

    @Inject
    private Main plugin;

    public HasteyBoysScenario() {
        super(ModalityType.SCENARIO, "hastey_boys", "&bHastey Boys", Material.DIAMOND_PICKAXE,
                ChatUtils.format("&7- Todas las herramientas tienen Eficiencia 3 e Irrompibilidad 1"));
    }

    @EventHandler
    public void onCraft(CraftItemEvent e){
        var material = e.getRecipe().getResult().getType();

        if(isTool(material)){
            e.getInventory().setResult(new ItemCreator(material).enchants(Enchantment.DIG_SPEED, 3).enchants(Enchantment.DURABILITY, 1).setPersistentData(plugin, "hastey_boys", PersistentDataType.STRING, "true"));
        }
    }

    @EventHandler
    public void onClick(InventoryClickEvent e){
        var current = e.getCurrentItem();

        if(current != null && isTool(current.getType()) && !(current.containsEnchantment(Enchantment.DIG_SPEED) || current.containsEnchantment(Enchantment.DURABILITY)) && !current.hasItemMeta()){
            e.setCurrentItem(new ItemCreator(current.getType()).enchants(Enchantment.DIG_SPEED, 3).enchants(Enchantment.DURABILITY, 1).setPersistentData(plugin, "hastey_boys", PersistentDataType.STRING, "true"));
        }
    }

    @EventHandler
    public void onPrepare(PrepareResultEvent event) {
        if(event.getInventory() == null) { return; }

        if(event.getInventory().getType() != InventoryType.GRINDSTONE) { return; }
        if(event.getResult() == null) { return; }

        var item = event.getResult();

        var persistentData = new ItemPersistentData(plugin, "hastey_boys", item.getItemMeta());

        if(!persistentData.hasData(PersistentDataType.STRING)) { return; }
        if(persistentData.getData(PersistentDataType.STRING) == null) { return; }

        if(persistentData.getData(PersistentDataType.STRING).equals("true")) {
            event.setResult(new ItemCreator(Material.AIR));
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
