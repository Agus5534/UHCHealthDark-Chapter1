package io.github.wickeddroidmx.plugin.modalities.uhc;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;

public class UhcVanillaMode extends Modality {
    public UhcVanillaMode() {
        super(ModalityType.UHC, "uhc_vanilla", "&bUhc Vanilla", Material.GOLDEN_APPLE,
                ChatUtils.format("&7- "));
    }
}
