package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class NoNetherMode extends Modality {
    public NoNetherMode() {
        super(ModalityType.MODE, "no_nether", "&cNo Nether", Material.NETHERRACK,
                ChatUtils.format("&7- No se puede entrar al nether"));
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            e.getPlayer().sendMessage(ChatUtils.PREFIX + "El nether esta desactivado.");
            e.setCancelled(true);
        }
    }
}
