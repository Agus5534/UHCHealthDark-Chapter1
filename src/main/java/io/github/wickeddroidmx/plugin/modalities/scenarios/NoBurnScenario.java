package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        name = "&cNo Burn",
        key = "no_burn",
        material = Material.LAVA_BUCKET,
        lore = {"&7- Los items no se queman."}
)
public class NoBurnScenario extends Modality {
    public NoBurnScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getEntityType() == EntityType.DROPPED_ITEM) {
            if(e.getCause() == EntityDamageEvent.DamageCause.FIRE
                    || e.getCause() ==  EntityDamageEvent.DamageCause.HOT_FLOOR
                    || e.getCause() ==  EntityDamageEvent.DamageCause.LAVA
                    || e.getCause() ==  EntityDamageEvent.DamageCause.FIRE_TICK) {
                e.setCancelled(true);
            }
        }
    }
}
