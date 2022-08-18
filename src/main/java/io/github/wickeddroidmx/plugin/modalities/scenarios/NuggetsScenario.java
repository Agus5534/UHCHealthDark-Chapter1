package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Creature;
import org.bukkit.entity.Monster;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import team.unnamed.gui.core.item.type.ItemBuilder;

public class NuggetsScenario extends Modality {
    public NuggetsScenario() {
        super(ModalityType.SCENARIO, "nuggets", "&cNuggets", Material.IRON_NUGGET,
                ChatUtils.format("&7- Si matas a un mob hostil te da 3 golden nuggets"),
                ChatUtils.format("&7- Si matas a un mob pacificio te da 3 iron nuggets"));
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
