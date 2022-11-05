package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.papermc.paper.event.player.PlayerTradeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.lang.instrument.IllegalClassFormatException;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "trade_scenario",
        name = "&aTrade Paranoia",
        material = Material.EMERALD,
        lore = {"&7- Al tradear con un aldeano hay un 7% de que salgan tus coords."}
)
public class TradeParanoiaMode extends Modality {
    public TradeParanoiaMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onPlayerTrade(PlayerTradeEvent e) {
        var player = e.getPlayer();
        var location = player.getLocation();

        if (ThreadLocalRandom.current().nextInt(1,100) <= 7) {
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);

            Bukkit.broadcast(Component.text(ChatUtils.format(String.format("&7[&cTrade Paranoia&7] &7» Coordenadas de &6%s &7| X: &6%d &7| Y: &6%d &7| Z: &6%d &7| Mundo: &6%s &7", player.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName()))));
        }
    }
}
