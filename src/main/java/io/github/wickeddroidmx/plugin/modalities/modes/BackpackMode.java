package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import team.unnamed.gui.core.item.type.ItemBuilder;

public class BackpackMode extends Modality {
    public BackpackMode() {
        super(ModalityType.MODE, "backpack", ChatUtils.format("&bBackPack"), Material.SHULKER_BOX,
                ChatUtils.format("&7- Todos recibiran una shulker box."));
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Bukkit.getOnlinePlayers().forEach(player -> player.getInventory().addItem(ItemBuilder.newBuilder(Material.SHULKER_BOX).build()));
    }

    @EventHandler
    public void onPlayerLaterScatter(PlayerLaterScatterEvent e) {
        var player = e.getPlayer();

        player.getInventory().addItem(ItemBuilder.newBuilder(Material.SHULKER_BOX).build());
    }
}
