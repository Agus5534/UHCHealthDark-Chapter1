package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.instrument.IllegalClassFormatException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(name = "&aAnvil Discounts", key = "anvil_discounts",
modalityType = ModalityType.SCENARIO, material = Material.ANVIL)
public class AnvilDiscountsScenario extends Modality {

    HashMap<ItemStack, Integer> itemsHash;

    public AnvilDiscountsScenario() throws IllegalClassFormatException {
        super(ChatUtils.format("&7- Todos los yunques tendrán hasta 55% de descuento"));
    }

    @EventHandler
    public void onAnvilPrepare(PrepareAnvilEvent event) {
        int repairCost = event.getInventory().getRepairCost();
        var itemOne = event.getInventory().getFirstItem();

        if(itemOne == null) { return; }

        if(itemsHash.containsKey(itemOne)) {
            event.getInventory().setRepairCost(itemsHash.get(itemOne));
            return;
        }

        double toDiscount = ThreadLocalRandom.current().nextDouble(0.45, 1.0);

        int newRepairCost = (int) Math.round(repairCost*toDiscount);

        if(newRepairCost < 1) { newRepairCost = 2; }

        event.getInventory().setRepairCost(newRepairCost);

        itemsHash.put(itemOne,newRepairCost);
    }

    @Override
    public void activeMode() {
        super.activeMode();
        itemsHash = new HashMap<>();
    }
}
