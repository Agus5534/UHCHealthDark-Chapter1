package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.papermc.paper.event.player.PlayerTradeEvent;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "no_villages",
        name = "&aNo Aldeas",
        material = Material.VILLAGER_SPAWN_EGG,
        lore = {"&7- Las aldeas se encontrarán desactivadas."}
)
public class NoVillagesMode extends Modality {

    public NoVillagesMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onPlayerTrade(PlayerTradeEvent e) {
        var player = e.getPlayer();

        player.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "Se encuentra activo el modo " + this.getName()));
        e.setCancelled(true);
    }
}
