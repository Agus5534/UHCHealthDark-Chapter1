package io.github.wickeddroidmx.plugin.modalities.uhc;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.UHC,
        key = "uhc_vanilla",
        name = "&bUHC Vanilla",
        material = Material.GOLDEN_APPLE,
        lore = {"&7- xD?"}
)
public class UhcVanillaMode extends Modality {
    public UhcVanillaMode() throws IllegalClassFormatException {
        super();
    }
}
