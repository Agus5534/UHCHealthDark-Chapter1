package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;

public class TeamInventoryScenario extends Modality {
    public TeamInventoryScenario() {
        super(ModalityType.SCENARIO, "team_inventory", "&aTeam Inventory", Material.CHEST,
                ChatUtils.format("&7- Hay un inventario por equipo, se usa con /ti"));
    }
}
