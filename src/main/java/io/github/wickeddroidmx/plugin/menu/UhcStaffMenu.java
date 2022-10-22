package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.events.game.ChangeGameTimeEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;

public class UhcStaffMenu {

    @Inject
    private GameManager gameManager;

    public Inventory getTimeInventory() {
        return GUIBuilder.builderStringLayout("Tiempos de UHC", 6)
                .setLayoutLines(
                        "CCCCCCCCC",
                        "CCCCDCCCC",
                        "CCCEFCCCC",
                        "CCCGHICCC",
                        "CCCJKCCCC",
                        "CCCCCCCCC"
                )
                .setLayoutItem('C', ItemClickable.builderCancellingEvent()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.GRAY)
                                .setName(" ")
                                .build())
                        .build())
                .setLayoutItem('D', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.PURPLE)
                                .setName(ChatUtils.format("&6Tiempo (MEETUP): "))
                                .setLore(ChatUtils.format("&75 minutos | Meetup"),
                                        ChatUtils.format("&730 segundos | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(300, 30));
                            gameManager.setBorderDelay(1200);
                            gameManager.setWorldBorder(750);
                            return true;
                        })
                        .build())
                .setLayoutItem('E', ItemClickable.builder()
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
                .setLayoutItem('F', ItemClickable.builder()
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
                .setLayoutItem('G', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.YELLOW)
                                .setName(ChatUtils.format(ChatUtils.format("&6Tiempo: ")))
                                .setLore(ChatUtils.format("&71 hora y 30 minutos | Meetup"),
                                        ChatUtils.format("&745 minutos | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(5400, 2700));

                            return true;
                        })
                        .build())
                .setLayoutItem('H', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.YELLOW)
                                .setName(ChatUtils.format("&6Tiempo: "))
                                .setLore(ChatUtils.format("&72 horas | Meetup"),
                                        ChatUtils.format("&745 minutos | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(7200, 2700));
                            gameManager.setBorderDelay(900);
                            return true;
                        })
                        .build())
                .setLayoutItem('I', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.YELLOW)
                                .setName(ChatUtils.format(ChatUtils.format("&6Tiempo: ")))
                                .setLore(ChatUtils.format("&72 horas | Meetup"),
                                        ChatUtils.format("&71 hora | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(7200, 3600));

                            return true;
                        })
                        .build())
                .setLayoutItem('J', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.RED)
                                .setName(ChatUtils.format(ChatUtils.format("&6Tiempo: ")))
                                .setLore(ChatUtils.format("&72 horas y 30 minutos | Meetup"),
                                        ChatUtils.format("&71 hora y 15 minutos | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(9000, 4500));

                            return true;
                        })
                        .build())
                .setLayoutItem('K', ItemClickable.builder()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("WOOL", DyeColor.RED)
                                .setName(ChatUtils.format("&6Tiempo: "))
                                .setLore(ChatUtils.format("&72 horas 55 minutos | Meetup"),
                                        ChatUtils.format("&71 hora 20 minutos | PvP"))
                                .build())
                        .setAction(event -> {
                            Bukkit.getPluginManager().callEvent(new ChangeGameTimeEvent(10500, 4800));
                            gameManager.setBorderDelay(300);
                            return true;
                        })
                        .build())
                .build();
    }
}
