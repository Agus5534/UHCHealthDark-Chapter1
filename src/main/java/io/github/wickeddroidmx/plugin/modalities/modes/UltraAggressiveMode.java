package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;

public class UltraAggressiveMode extends Modality {

    @Inject
    private GameManager gameManager;

    public UltraAggressiveMode() {
        super(ModalityType.MODE, "ultra_aggressive", "&cUltra Agresivo", Material.NETHERITE_SWORD,
                ChatUtils.format("&7- El pvp se activa a los 5 minutos"));
    }

    @Override
    public void activeMode() {
        super.activeMode();

        gameManager.setTimeForPvP(300);
    }

    @Override
    public void desactiveMode() {
        super.desactiveMode();

        gameManager.setTimeForPvP(3600);
    }
}
