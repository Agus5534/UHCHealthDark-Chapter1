package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;

import java.util.UUID;
import java.util.stream.Collectors;

public class UhcModeMenu {
    private GameManager gameManager;
    private ExperimentManager experimentManager;
    private ModeManager modeManager;
    private ListCache<UUID> ironManCache;

    public UhcModeMenu(ModeManager modeManager, GameManager gameManager, ExperimentManager experimentManager, ListCache<UUID> ironManCache) {
        this.modeManager = modeManager;
        this.gameManager = gameManager;
        this.experimentManager = experimentManager;
        this.ironManCache = ironManCache;
    }
    public Inventory getSelectInventory() {
        return MenuInventory.newStringLayoutBuilder("Modalidades", 3)
                .layoutLines(
                        "xxxxxxxxx",
                        "xmxsxtxcx",
                        "xxxxxxxxv"
                )
                .layoutItem('x', ItemClickable.onlyItem(new ItemCreator(Material.GRAY_STAINED_GLASS).name(" ")))
                .layoutItem('m', ItemClickable.builder().item(new ItemCreator(ModalityType.MODE.getMaterial()).name(ChatUtils.formatC("&bModalidades")).lore((modeManager.getModesActive(ModalityType.MODE).size() >= 1) ? modeManager.getModesActive(ModalityType.MODE).stream().map(Modality::getName).map(modality -> ChatUtils.format(String.format("&7- %s", modality))).collect(Collectors.joining()) : ChatUtils.format("&7No hay modalidades activas."))).action(action -> {
                    var player = action.getWhoClicked();

                    if (modeManager.getModesActive(ModalityType.MODE).size() == 0) {
                        player.sendMessage(ChatUtils.PREFIX + "No hay ningún scenario activo.");

                        return true;
                    }

                    player.getInventory().close();

                    player.openInventory(getModeInventory(ModalityType.MODE));
                    return true;
                }).build())
                .layoutItem('s', ItemClickable.builder().item(new ItemCreator(ModalityType.SCENARIO.getMaterial()).name(ChatUtils.formatC("&eScenarios")).lore((modeManager.getModesActive(ModalityType.SCENARIO).size() >= 1) ? modeManager.getModesActive(ModalityType.SCENARIO).stream().map(Modality::getName).map(modality -> ChatUtils.format(String.format("&7- %s", modality))).collect(Collectors.joining()) : ChatUtils.format("&7No hay scenarios activas."))).action( action -> {
                    var player = action.getWhoClicked();

                    if (modeManager.getModesActive(ModalityType.SCENARIO).size() == 0) {
                        player.sendMessage(ChatUtils.PREFIX + "No hay ningún scenario activo.");

                        return true;
                    }

                    player.getInventory().close();

                    player.openInventory(getModeInventory(ModalityType.SCENARIO));

                    return true;
                }).build())
                .layoutItem('t', ItemClickable.builder().item(new ItemCreator(ModalityType.TEAM.getMaterial()).name(ChatUtils.formatC("&bModalidades de equipo")).lore((modeManager.getModesActive(ModalityType.TEAM).size() >= 1) ? modeManager.getModesActive(ModalityType.TEAM).stream().map(Modality::getName).map(modality -> ChatUtils.format(String.format("&7- %s", modality))).collect(Collectors.joining()) : ChatUtils.format("&7No hay modalidades de equipo activas."))).action(action -> {
                    var player = action.getWhoClicked();

                    if (modeManager.getModesActive(ModalityType.TEAM).size() == 0) {
                        player.sendMessage(ChatUtils.PREFIX + "No hay ningún modo de equipo activo.");

                        return true;
                    }

                    player.getInventory().close();

                    player.openInventory(getModeInventory(ModalityType.TEAM));

                    return true;
                }).build())
                .layoutItem('c', ItemClickable.builder().item(new ItemCreator(ModalityType.SETTING.getMaterial()).name(ChatUtils.formatC("&dSettings")).lore((modeManager.getModesActive(ModalityType.SETTING).size() >= 1) ? modeManager.getModesActive(ModalityType.SETTING).stream().map(Modality::getName).map(modality -> ChatUtils.format(String.format("&7 - %s", modality))).collect(Collectors.joining()) : ChatUtils.format("&7No hay settings activas."))).action(action -> {
                    var player = action.getWhoClicked();

                    if (modeManager.getModesActive(ModalityType.SETTING).size() == 0) {
                        player.sendMessage(ChatUtils.PREFIX + "No hay ninguna setting activa");

                        return true;
                    }

                    player.getInventory().close();

                    player.openInventory(getModeInventory(ModalityType.SETTING));

                    return true;
                }).build())
                .layoutItem('v', ItemClickable.builder().item(new ItemCreator(Material.BARRIER).name(ChatUtils.formatC("&6Volver"))).action(action -> {
                    var player = action.getWhoClicked();

                    player.getInventory().close();

                    player.openInventory(new UhcMenu(gameManager, experimentManager, this, modeManager, ironManCache).getConfigMenu());

                    return true;
                }).build())
                .build();
    }

    public Inventory getModeInventory(ModalityType modalityType) {
        return MenuInventory.newPaginatedBuilder(Modality.class, modalityType.name()+"s enabled - %page%", 6)
                .itemIfNoEntities(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name("")))
                .entityParser(mode -> ItemClickable.builder().item(mode.build()).build())
                .nextPageItem(p -> ItemClickable.builder(53).item(new ItemCreator(Material.ARROW).name(ChatUtils.formatC("&6Siguiente página - " + p))).build())
                .previousPageItem(p -> ItemClickable.builder(53).item(new ItemCreator(Material.ARROW).name(ChatUtils.formatC("&6Anterior página - " + p))).build())
                .entities(modeManager.getModesActive(modalityType))
                .bounds(10, 35)
                .itemsPerRow(7)
                .build();
    }

}
