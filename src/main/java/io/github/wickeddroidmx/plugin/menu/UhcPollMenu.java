package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.poll.PollManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

public class UhcPollMenu {

    public Inventory getPollInventory(PollManager pollManager) {
        return MenuInventory.newStringLayoutBuilder(pollManager.getActivePoll().getQuestion(), 3)
                .layoutLines("xxxxxxxxx",
                             "xaxxxxxcx",
                             "xxxxxxxxx")
                .layoutItem('x', ItemClickable.builder().item(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(ChatColor.AQUA + pollManager.getActivePoll().getQuestion())).build())
                .layoutItem('a', ItemClickable.builder().item(new ItemCreator(Material.MAGENTA_CONCRETE).name(ChatColor.GOLD + pollManager.getActivePoll().getAns1())).action(e -> {
                    pollManager.getActivePoll().registerVote((Player)e.getWhoClicked(), 1);
                    e.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.CANT_USE);
                    Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + String.format("%s votó la opción '&6%s&7'",
                            e.getWhoClicked().getName(),
                            pollManager.getActivePoll().getAns1())));
                    return true;
                }).build())
                .layoutItem('c', ItemClickable.builder().item(new ItemCreator(Material.PURPLE_CONCRETE).name(ChatColor.GOLD + pollManager.getActivePoll().getAns2())).action(e -> {
                    pollManager.getActivePoll().registerVote((Player)e.getWhoClicked(), 2);
                    e.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.CANT_USE);
                    Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + String.format("%s votó la opción '&6%s&7'",
                            e.getWhoClicked().getName(),
                            pollManager.getActivePoll().getAns2())));
                    return true;
                }).build())
                .build();
    }
}
