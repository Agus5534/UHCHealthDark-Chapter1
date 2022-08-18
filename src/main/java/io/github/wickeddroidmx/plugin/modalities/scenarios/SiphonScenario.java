package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class SiphonScenario extends Modality {
    public SiphonScenario() {
        super(ModalityType.SCENARIO, "siphon", ChatUtils.format("&cSiphon"), Material.GOLDEN_APPLE,
                ChatUtils.format("&7- Al matar a alguien se te cura la vida si tienes menos de 5 corazones"),
                ChatUtils.format("&7- Si tienes más recibes 3 corazones de absorpción"));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        var killer = player.getKiller();

        if (killer != null) {
            if (killer.getHealth() <= 5) {
                killer.setHealth(Objects.requireNonNull(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH)).getBaseValue());
            } else {
                killer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 2));
            }
        }
    }
}
