package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&bSuspicious Stew Less",
        modalityType = ModalityType.SCENARIO,
        material = Material.SUSPICIOUS_STEW,
        key = "stew_less",
        experimental = true,
        lore = {"&7- Las Sopas Sospechosas estan vetadas"}
)
public class SuspiciousStewLessScenario extends Modality {

    public SuspiciousStewLessScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent event) {
        var item = event.getItem();

        if(item.getType() == Material.SUSPICIOUS_STEW) {
            event.setCancelled(true);
        }
    }
}
