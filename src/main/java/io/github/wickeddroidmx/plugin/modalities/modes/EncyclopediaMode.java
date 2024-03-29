package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.enchantment.EnchantItemEvent;

import java.lang.instrument.IllegalClassFormatException;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        name = "&5Encyclopedia",
        modalityType = ModalityType.MODE,
        key = "encyclopedia",
        material = Material.ENCHANTED_BOOK,
        lore = {"&7- Al encantar un libro este puede duplicarse"},
        experimental = true
)
public class EncyclopediaMode extends Modality {

    public EncyclopediaMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onEnchant(EnchantItemEvent event) {
        if(event.getItem() == null) { return; }

        if(event.getItem().getType() != Material.BOOK) { return; }

        int n = ThreadLocalRandom.current().nextInt(1,100);

        var player = event.getEnchanter();
        var enchantedItem = new ItemCreator(event.getItem().clone());

        enchantedItem.removeEnchantments();

        event.getItem().getEnchantments().forEach((enchantment, integer) -> {
            if(!enchantedItem.containsEnchantment(enchantment)) {
                enchantedItem.enchants(enchantment, integer);
            }
        });

        event.getEnchantsToAdd().forEach((enchantment, integer) -> {
            if(!enchantedItem.containsEnchantment(enchantment)) {
                enchantedItem.enchants(enchantment, integer);
            }
        });

        if(n > 93) {
            player.getInventory().addItem(enchantedItem);
        }

        if(n > 97) {
            player.getInventory().addItem(enchantedItem);
        }
    }
}
