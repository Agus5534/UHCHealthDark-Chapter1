package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.events.game.ChangeGameTimeEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.entity.Item;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;

public class UhcStaffMenu {

    @Inject
    private GameManager gameManager;

    public Inventory getTimeInventory() {
        return GUIBuilder.builderStringLayout("Tiempos de UHC", 3)
                .setLayoutLines(
                        "CCCCCCCCC",
                        "CCMHLTQCC",
                        "CCCCCCCCD"
                )
                .setLayoutItem('C', ItemClickable.builderCancellingEvent()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.GRAY)
                                .setName(" ")
                                .build())
                        .build())
                .setLayoutItem('M', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.GREEN)
                                .setName(ChatUtils.format(ChatUtils.format("&6Tiempo: ")))
                                .setLore(ChatUtils.format("&730 minutos | Meetup"),
                                        ChatUtils.format("&715 minutos | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(1800, 900));

                            return true;
                        })
                        .build())
                .setLayoutItem('H', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.GREEN)
                                .setName(ChatUtils.format(ChatUtils.format("&6Tiempo: ")))
                                .setLore(ChatUtils.format("&71 hora | Meetup"),
                                        ChatUtils.format("&730 minutos | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(3600, 1800));

                            return true;
                        })
                        .build())
                .setLayoutItem('L', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.GREEN)
                                .setName(ChatUtils.format(ChatUtils.format("&6Tiempo: ")))
                                .setLore(ChatUtils.format("&71 hora y 30 minutos | Meetup"),
                                        ChatUtils.format("&745 minutos | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(5400, 2700));

                            return true;
                        })
                        .build())
                .setLayoutItem('T', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.GREEN)
                                .setName(ChatUtils.format(ChatUtils.format("&6Tiempo: ")))
                                .setLore(ChatUtils.format("&72 horas | Meetup"),
                                        ChatUtils.format("&71 hora | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(7200, 3600));

                            return true;
                        })
                        .build())
                .setLayoutItem('Q', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.GREEN)
                                .setName(ChatUtils.format(ChatUtils.format("&6Tiempo: ")))
                                .setLore(ChatUtils.format("&72 horas y 30 minutos | Meetup"),
                                        ChatUtils.format("&71 hora y 15 minutos | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(9000, 4500));

                            return true;
                        })
                        .build())
                .setLayoutItem('D', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.RED)
                                .setName(ChatUtils.format("&cNo Tocar"))
                                .setLore(ChatUtils.format("&7Tiempo para probar cosas (Dev)"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(30, 15));

                            return true;
                        })
                        .build())
                .build();
    }
}
