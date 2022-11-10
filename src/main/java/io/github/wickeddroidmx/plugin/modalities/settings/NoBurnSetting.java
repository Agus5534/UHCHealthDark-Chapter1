package io.github.wickeddroidmx.plugin.modalities.settings;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.SETTING,
        name = "&cNo Burn",
        key = "no_burn",
        material = Material.LAVA_BUCKET,
        lore = {"&7- Los items no se queman."}
)
public class NoBurnSetting extends Modality {
    public NoBurnSetting() throws IllegalClassFormatException {
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
