package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Objects;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "tanks",
        name = "&cTanques",
        material = Material.NETHERITE_CHESTPLATE,
        lore = {"&7- Los jugadores tendrán doble barra de vida."}
)
public class TanksMode extends Modality {
    public TanksMode() throws IllegalClassFormatException {
        super();
    }


    @EventHandler
    public void onPlayerScatter(PlayerScatteredEvent e) {
        var player = e.getPlayer();

        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(40.0D);
        player.setHealth(40.0D);
    }

    @EventHandler
    public void onLaterScatter(PlayerLaterScatterEvent event) {
        var player = event.getPlayer();

        Objects.requireNonNull(player.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(40.0D);
        player.setHealth(40.0D);
    }
}
