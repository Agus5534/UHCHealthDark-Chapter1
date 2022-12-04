package io.github.wickeddroidmx.plugin.modalities.teams;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&bCaptains",
        key = "captains",
        modalityType = ModalityType.TEAM,
        material = Material.MAP,
        lore = {"&7- X jugadores serán seleccionados para elegir team."}
)
public class CaptainsMode extends Modality {
    public CaptainsMode() throws IllegalClassFormatException {
        super();
    }
}
