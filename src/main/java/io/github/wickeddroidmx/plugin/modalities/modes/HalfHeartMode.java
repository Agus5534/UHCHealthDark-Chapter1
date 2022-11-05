package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "half_heart",
        name = "&cHalf Heart",
        material = Material.GOLDEN_CARROT,
        lore = {"&7- Todos comenzarán con medio corazón y una notch"}
)
public class HalfHeartMode extends Modality {
    public HalfHeartMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        for (var player : Bukkit.getOnlinePlayers()) {
            player.setHealth(0.5D);

            player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
        }
    }

    @EventHandler
    public void onPlayerLaterScatter(PlayerLaterScatterEvent e) {
        var player = e.getPlayer();

        player.setHealth(0.5D);

        player.getInventory().addItem(new ItemStack(Material.ENCHANTED_GOLDEN_APPLE));
    }

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {
        var item = e.getItem();

        if (item.getType() == Material.SUSPICIOUS_STEW) {
            e.setCancelled(true);
        }
    }
}
