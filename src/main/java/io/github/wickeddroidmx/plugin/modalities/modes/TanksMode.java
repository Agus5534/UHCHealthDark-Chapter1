package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Objects;

public class TanksMode extends Modality {
    public TanksMode() {
        super(ModalityType.MODE, "tanks", "&cTanques", Material.DIAMOND,
                ChatUtils.format("&7- Los jugadores tienen doble barra de vida."));
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
