package io.github.wickeddroidmx.plugin.modalities.teams;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&bChosen",
        modalityType = ModalityType.TEAM,
        experimental = true,
        key = "chosen",
        material = Material.BEACON,
        lore = {
                "&7- Los jugadores podrán elegir a su team",
                "&7- Habilita &6/team found",
                "&7- Habilita &6/team invite"
        }
)
public class ChosenMode extends Modality {
    public ChosenMode() throws IllegalClassFormatException {
        super();
    }
}
