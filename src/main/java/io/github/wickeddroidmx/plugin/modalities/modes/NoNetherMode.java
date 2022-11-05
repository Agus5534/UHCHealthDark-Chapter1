package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "no_nether",
        name = "&cNo Nether",
        material = Material.NETHERRACK,
        lore = {"&7- El nether se encontrará desactivado."}
)
public class NoNetherMode extends Modality {
    public NoNetherMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            e.getPlayer().sendMessage(ChatUtils.PREFIX + "El nether esta desactivado.");
            e.setCancelled(true);
        }
    }
}
