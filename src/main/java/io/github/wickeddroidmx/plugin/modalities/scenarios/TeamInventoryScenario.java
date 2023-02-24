package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.configurable.ConfigurableModality;
import org.bukkit.Material;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.List;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        key = "team_inventory",
        name = "&aTeam Inventory",
        material = Material.ENDER_CHEST,
        lore = {"&7- Hay un inventario por equipo, se usa con &b/ti"}
)
public class TeamInventoryScenario extends Modality implements ConfigurableModality {
    public TeamInventoryScenario() throws IllegalClassFormatException {
        super();
    }
    int enabledOption;

    @Override
    public void activeMode() {
        super.activeMode();

        enabledOption = 27;
    }

    @Override
    public List<Integer> optionList() {
        return options;
    }

    @Override
    public void setOption(Integer option) {
        this.enabledOption = option;
    }

    @Override
    public Integer getOption() {
        return enabledOption;
    }
}
