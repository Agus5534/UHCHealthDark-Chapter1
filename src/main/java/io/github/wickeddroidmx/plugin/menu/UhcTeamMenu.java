package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.menu.item.ItemClickable;
import team.unnamed.gui.menu.type.MenuInventory;
import javax.inject.Inject;
import java.util.Comparator;
import java.util.stream.Collectors;

public class UhcTeamMenu {

    @Inject
    private TeamManager teamManager;

    public Inventory getTeamList() {
        return MenuInventory.newPaginatedBuilder(UhcTeam.class, "Teams existentes - %page%")
                .itemIfNoEntities(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .entityParser(team -> ItemClickable.onlyItem(new ItemCreator(Material.PLAYER_HEAD).setSkullSkin(team.getOwner()).name(ChatColor.GOLD + team.getName()).lore((Component) team.getTeamPlayers().stream().map(Bukkit::getOfflinePlayer).map(offlinePlayer -> ChatUtils.format(String.format("&7- %s", offlinePlayer.getName()))).collect(Collectors.toList()))))
                .nextPageItem(page -> ItemClickable.builder(53).item(new ItemCreator(Material.ARROW).name(ChatColor.GOLD + "Siguiente Página - " + page)).build())
                .previousPageItem(page -> ItemClickable.builder(53).item(new ItemCreator(Material.ARROW).name(ChatColor.GOLD + "Anterior Página - " + page)).build())
                .itemIfNoNextPage(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .bounds(10, 44)
                .itemsPerRow(7)
                .entities(teamManager.getUhcTeams().values().stream().filter(t -> t.isAlive()).toList())
                .build();
    }

    public Inventory getJoinInventory(Player sender, Player target) {
        return MenuInventory.newPaginatedBuilder(UhcTeam.class, "Teams")
                .entities(teamManager.getUhcTeams().values().stream().sorted(Comparator.comparing(uhcTeam -> uhcTeam.getOwner().getName())).toList())
                .entityParser(uhcTeam -> ItemClickable.builder().item(new ItemCreator(Material.PLAYER_HEAD).setSkullSkin(uhcTeam.getOwner()).name(ChatColor.GOLD + uhcTeam.getName() + " Team")).action(e -> {
                    Bukkit.getPluginManager().callEvent(new PlayerJoinedTeamEvent(uhcTeam, target));
                    sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("El jugador &6%s &7se ha unido al equipo de &6%s", target.getName(), uhcTeam.getOwner().getName())));

                    return true;
                }).build())
                .bounds(10, 44)
                .itemsPerRow(7)
                .nextPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.ARROW).name(ChatColor.GOLD + "Siguiente Página - " + p)))
                .previousPageItem(p -> ItemClickable.onlyItem(new ItemCreator(Material.ARROW).name(ChatColor.GOLD + "Anterior Página - " + p)))
                .itemIfNoNextPage(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .itemIfNoPreviousPage(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .itemIfNoEntities(ItemClickable.onlyItem(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name(" ")))
                .build();
    }
}
