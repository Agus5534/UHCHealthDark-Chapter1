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
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;

import javax.inject.Inject;

public class UhcPollMenu {

    public Inventory getPollInventory(PollManager pollManager) {
        return GUIBuilder
                .builderStringLayout("Encuesta: " + pollManager.getActivePoll().getQuestion(), 3)
                .setLayoutLines("xxxxxxxxx",
                                "xaxxxxxcx",
                                "xxxxxxxxx")
                .setLayoutItem('x', ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.GRAY_STAINED_GLASS_PANE).name(ChatColor.AQUA + pollManager.getActivePoll().getQuestion())).build())
                .setLayoutItem('a', ItemClickable.builder().setItemStack(new ItemCreator(Material.MAGENTA_CONCRETE).name(ChatColor.GOLD + pollManager.getActivePoll().getAns1())).setAction(e -> {
                    pollManager.getActivePoll().registerVote((Player)e.getWhoClicked(), 1);
                    e.getWhoClicked().closeInventory(InventoryCloseEvent.Reason.CANT_USE);
                    Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + String.format("%s votó la opción '&6%s&7'",
                            e.getWhoClicked().getName(),
                            pollManager.getActivePoll().getAns1())));
                    return true;
                }).build())
                .setLayoutItem('c', ItemClickable.builder().setItemStack(new ItemCreator(Material.PURPLE_CONCRETE).name(ChatColor.GOLD + pollManager.getActivePoll().getAns2())).setAction(e -> {
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
