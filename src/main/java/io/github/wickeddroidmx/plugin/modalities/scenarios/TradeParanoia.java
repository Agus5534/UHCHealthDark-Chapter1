package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.papermc.paper.event.player.PlayerTradeEvent;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;

import java.util.Random;

public class TradeParanoia extends Modality {
    public TradeParanoia() {
        super(ModalityType.SCENARIO, "trade_paranoia", "&aTrade Paranoia",  Material.EMERALD,
                ChatUtils.format("&7- Al tradear con un aldeano hay un 5% de que salgan tus coords."));
    }

    @EventHandler
    public void onPlayerTrade(PlayerTradeEvent e) {
        var player = e.getPlayer();
        var random = new Random();
        var location = player.getLocation();

        if (random.nextInt(100) >= 95) {
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);

            Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format(String.format("Coordenadas de &6%s &7| X: &6%d &7| Y: &6%d &7| Z: &6%d &7| Mundo: &6%s &7", player.getName(), location.getBlockX(), location.getBlockY(), location.getBlockZ(), location.getWorld().getName()))));
        }
    }
}
