package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scoreboard.Team;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Comparator;
import java.util.UUID;
import java.util.stream.Collectors;

public class UhcMenu {

    @Inject
    @Named("ironman-cache")
    private ListCache<UUID> ironManCache;

    @Inject
    private GameManager gameManager;

    @Inject
    private TeamManager teamManager;

    public Inventory getIronManMenu() {
        return GUIBuilder.builderPaginated(OfflinePlayer.class, "IronMans - %page%")
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
                .setItemParser(offlinePlayer -> ItemClickable.builderCancellingEvent().setItemStack(
                        ItemBuilder.newSkullBuilder(1)
                                .setOwner(offlinePlayer.getName())
                                .setName(ChatColor.GOLD + offlinePlayer.getName())
                                .build()
                ).build())
                .setNextPageItem(page -> ItemClickable.builderCancellingEvent(53)
                        .setItemStack(ItemBuilder.newBuilder(Material.ARROW)
                                .setName("Siguiente pagina - " + page)
                                .build()
                        )
                        .build())
                .setPreviousPageItem(page -> ItemClickable.builderCancellingEvent(45)
                        .setItemStack(ItemBuilder.newBuilder(Material.ARROW)
                                .setName("Anterior pagina - " + page)
                                .build()
                        )
                        .build()
                )
                .setEntities(ironManCache.values()
                        .stream()
                        .map(Bukkit::getOfflinePlayer)
                        .collect(Collectors.toList()))
                .setItemIfNotEntities(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(ChatUtils.formatC("&6Vacío"))).build())
                .setBounds(10, 35)
                .setItemsPerRow(7)
                .build();
    }

    public Inventory getConfigMenu() {
        return GUIBuilder.builderStringLayout("UHC Config", 3)
                .setLayoutLines("xxxxxxxxx",
                                "xxxxxxxxx",
                                "xxxxxxxxx")
                .setLayoutItem('x', ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder
                                .newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.GRAY)
                                .setName("")
                                .build())
                        .build())
                .build();
    }
}
