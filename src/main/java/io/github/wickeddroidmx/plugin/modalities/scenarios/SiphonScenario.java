package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Objects;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        key = "siphon",
        name = "&cSiphon",
        material = Material.GOLDEN_APPLE,
        lore = {
                "&7- Al matar a alguien se te cura la vida si tienes menos de 5 corazones",
                "&7- Si tienes más recibes 3 corazones de Absorption"
        }
)
public class SiphonScenario extends Modality {
    public SiphonScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        var killer = player.getKiller();

        if (killer != null) {
            if (killer.getHealth() <= 10.0) {
                killer.setHealth(killer.getHealth()+5.0);
            } else {
                killer.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 200, 2));
            }
        }
    }
}
