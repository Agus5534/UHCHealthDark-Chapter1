package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

public class FireLessScenario extends Modality {
    public FireLessScenario() {
        super(ModalityType.SCENARIO, "fire_less", "&cFireLess", Material.LAVA_BUCKET,
                ChatUtils.format("&7- El fuego no hace daño"));
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        var entity = e.getEntity();

        if (entity instanceof Player player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.FIRE
                    || e.getCause() == EntityDamageEvent.DamageCause.FIRE_TICK
                    || e.getCause() == EntityDamageEvent.DamageCause.LAVA
                    || e.getCause() == EntityDamageEvent.DamageCause.HOT_FLOOR) {
                e.setCancelled(true);
            }
        }
    }
}
