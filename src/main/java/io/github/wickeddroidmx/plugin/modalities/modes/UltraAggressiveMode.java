package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "ultra_aggresive",
        name = "&cUltra Agresivo",
        material = Material.STONE_SWORD,
        lore = {"&7- El PvP se activa a los 5 minutos."}
)
public class UltraAggressiveMode extends Modality {

    @Inject
    private GameManager gameManager;

    public UltraAggressiveMode() throws IllegalClassFormatException {
        super();
    }

    @Override
    public void activeMode() {
        super.activeMode();

        gameManager.setTimeForPvP(300);
    }

    @Override
    public void deactivateMode() {
        super.deactivateMode();

        gameManager.setTimeForPvP(3600);
    }
}
