package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        key = "fire_less",
        name = "&cFireLess",
        material = Material.LAVA_BUCKET,
        lore = {"&7- El fuego no hace daño."}
)
public class FireLessScenario extends Modality {
    public FireLessScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        var entity = e.getEntity();

        if (entity instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE
                    || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                    || e.getCause() == EntityDamageEvent.DamageCause.LAVA
                    || e.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR) {
                e.setCancelled(true);
            }
        }
    }
}
