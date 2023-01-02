package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import team.unnamed.gui.core.item.type.ItemBuilder;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        key = "nuggets",
        name = "&cNuggets",
        material = Material.GOLD_NUGGET,
        lore = {
                "&7- Si matas a un mob hostil te da 3 golden nuggets.",
                "&7- Si matas a un mob pacífico te da 3 iron nuggets."
        }
)
public class NuggetsScenario extends Modality {
    public NuggetsScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        var entity = e.getEntity();

        if (entity instanceof Creature) {
            if (entity instanceof Monster) {
                e.getDrops().add(ItemBuilder.newBuilder(Material.GOLD_NUGGET, 3).build());
            } else {
                e.getDrops().add(ItemBuilder.newBuilder(Material.IRON_NUGGET, 3).build());
            }
        }
    }
}
