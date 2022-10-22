package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class UhcModeMenu {

    @Inject
    private ModeManager modeManager;

    public Inventory getSelectInventory() {
        return GUIBuilder
                .builderStringLayout("Modalidades", 3)
                .setLayoutLines(
                        "xxxxxxxxx",
                        "xmxxsxxtx",
                        "xxxxxxxxx"
                )
                .setLayoutItem('x', ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder.newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.GRAY)
                                .setName(" ")
                                .build())
                        .build())
                .setLayoutItem('m', ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder.newBuilder(Material.DIAMOND_SWORD)
                                .setName(ChatUtils.format("&bModalidades"))
                                .setLore((modeManager.getModesActive(ModalityType.MODE).size() > 1) ? modeManager
                                        .getModesActive(ModalityType.MODE)
                                        .stream()
                                        .map(Modality::getName)
                                        .map(modality -> ChatUtils.format(String.format("&7- %s", modality)))
                                        .collect(Collectors.joining())
                                        : ChatUtils.format("&7No hay modalidades activas."))
                                .build())
                        .setAction(action -> {
                            var player = action.getWhoClicked();

                            if (modeManager.getModesActive(ModalityType.MODE).size() == 0) {
                                player.sendMessage(ChatUtils.PREFIX + "No hay ningún scenario activo.");

                                return true;
                            }

                            player.getInventory().close();

                            player.openInventory(getModeInventory(ModalityType.MODE));
                            return true;
                        })
                        .build())
                .setLayoutItem('s', ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder.newBuilder(Material.DIAMOND)
                                .setName(ChatUtils.format("&eScenarios"))
                                .setLore((modeManager.getModesActive(ModalityType.SCENARIO).size() > 1) ? modeManager
                                        .getModesActive(ModalityType.SCENARIO)
                                        .stream()
                                        .map(Modality::getName)
                                        .map(modality -> ChatUtils.format(String.format("&7- %s", modality)))
                                        .collect(Collectors.joining())
                                        : ChatUtils.format("&7No hay modalidades activas."))
                                .build())
                        .setAction(action -> {
                            var player = action.getWhoClicked();

                            if (modeManager.getModesActive(ModalityType.SCENARIO).size() == 0) {
                                player.sendMessage(ChatUtils.PREFIX + "No hay ningún scenario activo.");

                                return true;
                            }

                            player.getInventory().close();

                            player.openInventory(getModeInventory(ModalityType.SCENARIO));

                            return true;
                        })
                        .build())
                .setLayoutItem('t', ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder.newBuilder(Material.POPPY)
                                .setName(ChatUtils.format("&bModalidades de equipo"))
                                .setLore((modeManager.getModesActive(ModalityType.TEAM).size() > 1) ? modeManager
                                        .getModesActive(ModalityType.TEAM)
                                        .stream()
                                        .map(Modality::getName)
                                        .map(modality -> ChatUtils.format(String.format("&7- %s", modality)))
                                        .collect(Collectors.joining())
                                        : ChatUtils.format("&7No hay modalidades activas."))
                                .build())
                        .setAction(action -> {
                            var player = action.getWhoClicked();

                            if (modeManager.getModesActive(ModalityType.TEAM).size() == 0) {
                                player.sendMessage(ChatUtils.PREFIX + "No hay ningún modo de equipo activo.");

                                return true;
                            }

                            player.getInventory().close();

                            player.openInventory(getModeInventory(ModalityType.TEAM));

                            return true;
                        })
                        .build())
                .build();
    }

    public Inventory getModeInventory(ModalityType modalityType) {
        return GUIBuilder
                .builderPaginated(Modality.class, "Modos de Juego activados - %page%", 6)
                .fillBorders(ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder
                                .newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.BLACK)
                                .setName(" ")
                                .build())
                        .build()
                )
                .setItemIfNotEntities(ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder
                                .newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.LIGHT_GRAY)
                                .setName("")
                                .build())
                        .build())
                .setItemParser(mode -> ItemClickable.builderCancellingEvent().setItemStack(mode.build()).build())
                .setNextPageItem(page -> ItemClickable.builder(53)
                        .setItemStack(ItemBuilder.newBuilder(Material.ARROW)
                                .setName("Siguiente pagina - " + page)
                                .build()
                        )
                        .build())
                .setPreviousPageItem(page -> ItemClickable.builder(45)
                        .setItemStack(ItemBuilder.newBuilder(Material.ARROW)
                                .setName("Anterior pagina - " + page)
                                .build()
                        )
                        .build()
                )
                .setEntities(modeManager.getModesActive(modalityType))
                .setBounds(10, 35)
                .setItemsPerRow(7)
                .build();
    }

}
