package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import javax.inject.Named;
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
        return GUIBuilder.builderPaginated(Player.class, "IronMans - %page%")
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
                .setItemParser(player -> ItemClickable.builder().setItemStack(
                        ItemBuilder.newSkullBuilder(1)
                                .setOwner(player.getName())
                                .setName(ChatColor.GOLD + player.getName())
                                .build()
                ).build())
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
                .setEntities(ironManCache.values()
                        .stream()
                        .map(Bukkit::getPlayer)
                        .collect(Collectors.toList()))
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
