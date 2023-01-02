package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        key = "team_inventory",
        name = "&aTeam Inventory",
        material = Material.ENDER_CHEST,
        lore = {"&7- Hay un inventario por equipo, se usa con &b/ti"}
)
public class TeamInventoryScenario extends Modality {
    public TeamInventoryScenario() throws IllegalClassFormatException {
        super();
    }
}
