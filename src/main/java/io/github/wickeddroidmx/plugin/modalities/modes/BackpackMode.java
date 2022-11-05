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
import team.unnamed.gui.core.item.type.ItemBuilder;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "backpack",
        name = "&bBackPack",
        material = Material.SHULKER_BOX,
        lore = {"&7- Todos inician con una shulker box"}
)
public class BackpackMode extends Modality {
    public BackpackMode() throws IllegalClassFormatException {
        super();
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
