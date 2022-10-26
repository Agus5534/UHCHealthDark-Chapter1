package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;

public class UhcStaffModesMenu {

    @Inject
    private GameManager gameManager;

    @Inject
    private ModeManager modeManager;

    public Inventory getModeInventory(ModalityType modalityType) {
        return GUIBuilder.builderPaginated(Modality.class, "Modalidades - %page%")
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
                .setItemParser(mode -> ItemClickable.builder().setItemStack(mode.build()).setAction(event -> {
                    var ic = new ItemCreator(event.getCurrentItem());

                    if(!mode.isEnabled()) {
                        if (modalityType == ModalityType.SCENARIO && gameManager.isScenarioLimit() && modeManager.getModesActive(ModalityType.SCENARIO).size() > 0)
                            return true;

                        mode.activeMode();

                        var itMeta = event.getCurrentItem().getItemMeta();
                        itMeta.addEnchant(Enchantment.CHANNELING,1,false);

                        event.getCurrentItem().setItemMeta(itMeta);
                    } else
                        mode.desactiveMode();

                        ic.removeEnchantments();

                        event.setCurrentItem(ic);

                    return true;
                }).build())
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
                .setEntities(modeManager.getAllModes(modalityType))
                .setBounds(10, 44)
                .setItemsPerRow(7)
                .build();
    }

    public Inventory getUHCInventory() {
        return GUIBuilder.builderPaginated(Modality.class, "Modalidades de UHC - %page%", 3)
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
                .setItemParser(mode -> ItemClickable.builder().setItemStack(mode.build()).setAction(event -> {
                    if(!mode.isEnabled())
                        mode.activeMode();
                    else
                        mode.desactiveMode();

                    return true;
                }).build())
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
                .setEntities(modeManager.getAllModes(ModalityType.UHC))
                .setBounds(10, 17)
                .setItemsPerRow(7)
                .build();
    }
}
