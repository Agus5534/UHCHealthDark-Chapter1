package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.sql.SQLConsults;
import io.github.wickeddroidmx.plugin.sql.StatsUser;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.DyeColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;

public class UhcStatsMenu {

    @Inject
    private SQLConsults sqlConsults;

    public Inventory getStatsInventory(OfflinePlayer player) {
        return GUIBuilder.builderStringLayout("Estadisticas de " + player.getName(), 3)
                .setLayoutLines(
                        "XXXXCXXXX",
                        "XXWXKXIXX",
                        "XXXXXXXXX"
                )
                .setLayoutItem('X', ItemClickable.builderCancellingEvent()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.GRAY)
                                .setName(" ")
                                .build())
                        .build())
                .setLayoutItem('C', ItemClickable.builderCancellingEvent()
                        .setItemStack(ItemBuilder.newSkullBuilder(1)
                                .setOwner(player.getName())
                                .setName(ChatUtils.format("&6Estadisticas de " + player.getName()))
                                .setLore(ChatUtils.format(String.format("&cKills: %d", sqlConsults.getUserStat(player.getUniqueId().toString(), StatsUser.KILLS))),
                                        ChatUtils.format(String.format("&eWins: %d", sqlConsults.getUserStat(player.getUniqueId().toString(), StatsUser.WINS))),
                                        ChatUtils.format(String.format("&7Iron Mans: %d", sqlConsults.getUserStat(player.getUniqueId().toString(), StatsUser.IRON_MAN))))
                                .build())
                        .build())
                .build();
    }
}
