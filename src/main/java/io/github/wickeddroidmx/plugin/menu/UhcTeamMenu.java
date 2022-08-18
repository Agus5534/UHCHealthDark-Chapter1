package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.Objects;
import java.util.stream.Collectors;

public class UhcTeamMenu {

    @Inject
    private TeamManager teamManager;

    public Inventory getTeamList() {
        return GUIBuilder.builderPaginated(UhcTeam.class, "Teams creados - %page%")
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
                .setItemParser(team -> ItemClickable.builderCancellingEvent().setItemStack(ItemBuilder
                                .newSkullBuilder(1)
                                .setOwner(team.getOwner().getName())
                                .setName(ChatColor.GOLD + team.getName())
                                .setLore(team.getTeamPlayers()
                                        .stream()
                                        .map(Bukkit::getOfflinePlayer)
                                        .map(player -> ChatUtils.format(String.format("&7- %s", player.getName())))
                                        .collect(Collectors.toList()))
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
                .setEntities(teamManager.getUhcTeams().values())
                .setBounds(10, 35)
                .setItemsPerRow(7)
                .build();
    }

    public Inventory getJoinInventory(Player sender, Player target) {
        return  GUIBuilder.builderPaginated(UhcTeam.class, "Teams")
                .setEntities(teamManager.getUhcTeams().values())
                .setItemParser(uhcTeam -> ItemClickable.builderCancellingEvent()
                        .setItemStack(ItemBuilder.newSkullBuilder(uhcTeam.getSize()).setName(ChatColor.GOLD + "Team de " + uhcTeam.getName()).setOwner(uhcTeam.getOwner().getName()).build())
                        .setAction(inventoryClickEvent -> {
                            Bukkit.getPluginManager().callEvent(new PlayerJoinedTeamEvent(uhcTeam, target));
                            sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("El jugador &6%s &7se ha unido al equipo de &6%s", target.getName(), uhcTeam.getOwner().getName())));

                            return true;
                        })
                        .build())
                .setBounds(10, 35)
                .setItemsPerRow(7)
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
                .setItemIfNotEntities(ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder
                                .newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.LIGHT_GRAY)
                                .setName("")
                                .build())
                        .build())
                .fillBorders(ItemClickable
                        .builderCancellingEvent()
                        .setItemStack(ItemBuilder
                                .newDyeItemBuilder("STAINED_GLASS_PANE", DyeColor.BLACK)
                                .setName(" ")
                                .build())
                        .build()
                )
                .build();
    }
}
