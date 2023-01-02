package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(name = "&8More Ores",key = "more_ores",modalityType = ModalityType.MODE, material = Material.COAL_ORE,
lore = {"&7- Aumenta la Ore Gen."})
public class MoreOresMode extends Modality {

    @Inject
    private Main plugin;

    public MoreOresMode() throws IllegalClassFormatException {
        super();
    }

}
