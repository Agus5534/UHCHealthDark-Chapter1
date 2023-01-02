package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        name = "&dHeavy Pockets",
        key = "heavy_pockets",
        material = Material.NETHERITE_SCRAP,
        experimental = true,
        lore = {"&7- Al morir dropea 2 Netherite Scraps"}
)
public class HeavyPocketsScenario extends Modality {

    @Inject
    private ModeManager modeManager;

    public HeavyPocketsScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        if(modeManager.isActiveMode("grave_robbers")) {
            return;
        }

        event.getEntity().getLocation().getWorld().dropItem(event.getEntity().getLocation(), new ItemCreator(Material.NETHERITE_SCRAP).amount(2));
    }

}
