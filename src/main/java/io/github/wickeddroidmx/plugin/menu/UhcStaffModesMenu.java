package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.menu.adapt.MenuInventoryWrapper;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import team.unnamed.gui.menu.type.MenuInventoryBuilder;

import javax.inject.Inject;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class UhcStaffModesMenu {

    @Inject
    private GameManager gameManager;

    @Inject
    private ModeManager modeManager;

    public Inventory getModeInventory(ModalityType modalityType) {
        String guiNAME = modalityType.name().toUpperCase() + "S Menu - %page%";
        return MenuInventory.newPaginatedBuilder(Modality.class, guiNAME)
                .itemIfNoEntities(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .entityParser(modality -> ItemClickable.builder().item(modality.build()).action(event -> {
                    var ic = new ItemCreator(event.getCurrentItem());

                    if(!modality.isEnabled()) {
                        if (modalityType == ModalityType.SCENARIO && gameManager.isScenarioLimit() && modeManager.getModesActive(ModalityType.SCENARIO).size() > 1)
                            return true;

                        modality.activeMode();

                        var itMeta = event.getCurrentItem().getItemMeta();
                        itMeta.addEnchant(Enchantment.CHANNELING,1,false);

                        event.getCurrentItem().setItemMeta(itMeta);
                    } else
                        modality.deactivateMode();

                    ic.removeEnchantments();

                    event.setCurrentItem(ic);

                    return true;
                }).build())
                .nextPageItem(page -> ItemClickable.of(46, new ItemCreator(Material.ARROW).name(ChatUtils.formatC("&6Siguiente página - " + page))))
                .previousPageItem(page -> ItemClickable.of(46, new ItemCreator(Material.ARROW).name(ChatUtils.formatC("&6Anterior página - " + page))))
                .itemIfNoNextPage(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .entities(modeManager.getAllModes(modalityType).stream().sorted(Comparator.comparing(Modality::isEnabled)).collect(Collectors.toList()))
                .bounds(10, 44)
                .itemsPerRow(7)
                .item(this.getClickable(modalityType, ModalityType.MODE), 47)
                .item(this.getClickable(modalityType, ModalityType.SCENARIO), 48)
                .item(this.getClickable(modalityType, ModalityType.UHC), 49)
                .item(this.getClickable(modalityType, ModalityType.TEAM), 50)
                .item(this.getClickable(modalityType, ModalityType.SETTING), 51)
                .build();

    }


    private ItemClickable getClickable(ModalityType actual, ModalityType modalityType) {
        String name = (modalityType == actual ? "Recargar Pestaña" : modalityType.name());
        ItemCreator itemCreator = new ItemCreator(modalityType.getMaterial()).name(ChatColor.AQUA + name);
        if(modalityType == actual) { itemCreator.looksEnchanted(); }

        return ItemClickable.builder().item(itemCreator)
                .action(e -> {
                    e.getWhoClicked().openInventory(getModeInventory(modalityType));
                    return true;
                })
                .build();
    }
}
