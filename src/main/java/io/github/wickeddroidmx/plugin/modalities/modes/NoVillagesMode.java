package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.papermc.paper.event.player.PlayerTradeEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class NoVillagesMode extends Modality {

    public NoVillagesMode() {
        super(ModalityType.MODE, "no_villages", "&aNo Villas", Material.EMERALD,
                ChatUtils.format("&7- Las villas se encuentran desactivadas"));
    }

    @EventHandler
    public void onPlayerTrade(PlayerTradeEvent e) {
        var player = e.getPlayer();

        player.sendMessage(ChatUtils.PREFIX + "Se encuentra activo el no aldeas.");
        e.setCancelled(true);
    }
}
