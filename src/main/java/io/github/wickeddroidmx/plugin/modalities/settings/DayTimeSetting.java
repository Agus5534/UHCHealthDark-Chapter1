package io.github.wickeddroidmx.plugin.modalities.settings;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&7Day Time",
        key = "day_time",
        lore = "&7- La hora del día sale en el scoreboard",
        modalityType = ModalityType.SETTING,
        material = Material.CLOCK
)
public class DayTimeSetting extends Modality {

    public DayTimeSetting() throws IllegalClassFormatException {
        super();
    }
}
