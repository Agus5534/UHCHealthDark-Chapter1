package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.ItemStack;

public class ChickenScenario extends Modality {
    public ChickenScenario() {
        super(ModalityType.SCENARIO, "chicken", "&cChicken", Material.EGG,
                ChatUtils.format("&7- Todos tendrán medio corazón y una notch"));
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
