package io.github.wickeddroidmx.plugin.modalities.teams;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(name = "&bChosen", key = "chosen", modalityType = ModalityType.TEAM, material = Material.ARROW)
public class ChosenMode extends Modality {
    public ChosenMode() throws IllegalClassFormatException {
        super(ChatUtils.format("&7- X jugadores seran seleccionados para elegir team"));
    }
}