package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.*;

public class DeathListMode extends Modality {

    @Inject
    private TeamManager teamManager;

    public DeathListMode() {
        super(ModalityType.MODE, "death_list", "&cDeath List", Material.PAPER,
                ChatUtils.format("&7- Al iniciar la partida se te dará un papel con un nombre"),
                ChatUtils.format("&7- Al matar a esa persona se te dará una recompensa"));
    }

    @EventHandler
    public void onPlayerScatter(PlayerScatteredEvent e) {
        givePaper(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLaterScatter(PlayerLaterScatterEvent e) {
        givePaper(e.getPlayer());
    }

    private void givePaper(Player player) {
        var uhcTeam = teamManager.getPlayerTeam(player.getUniqueId());

        if (uhcTeam != null) {
            var pl = Bukkit.getOnlinePlayers().stream().toList();

            for(Player p : pl) {
                if(p.getGameMode() == GameMode.SPECTATOR || uhcTeam.getTeamPlayers().contains(p.getUniqueId())) {
                    pl.remove(p);
                }
            }

            if(pl.size() <= 1) {
                return;
            }

            var randomPlayer = pl.get(new Random().nextInt(pl.size()));

            player.getInventory().addItem(
                    ItemBuilder
                            .newBuilder(Material.PAPER)
                            .setName(ChatUtils.format("&cDeath List"))
                            .setLore(ChatColor.GRAY + randomPlayer.getName())
                            .build());
        }
           /* Bukkit.getOnlinePlayers()
                    .stream()
                    .filter(randomPlayer -> !uhcTeam.getTeamPlayers().contains(randomPlayer.getUniqueId()))
                    .filter(randomPlayer -> randomPlayer.getGameMode() != GameMode.SPECTATOR)
                    .findAny()
                    .ifPresent(randomPlayer -> {
                        player.getInventory().addItem(
                                ItemBuilder
                                        .newBuilder(Material.PAPER)
                                        .setName(ChatUtils.format("&cDeath List"))
                                        .setLore(ChatColor.GRAY + randomPlayer.getName())
                                        .build());
                    });*/
    }
}
