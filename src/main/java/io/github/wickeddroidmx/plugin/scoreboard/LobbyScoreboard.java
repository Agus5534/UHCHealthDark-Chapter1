package io.github.wickeddroidmx.plugin.scoreboard;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.listeners.players.PlayerRankProfileRequestListener;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;

public class LobbyScoreboard extends UHCScoreboard {

    public LobbyScoreboard(Main plugin, Player player, ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        super(plugin, player, modeManager, gameManager, playerManager, teamManager);
    }

    @Override
    public void update(Main plugin, ModeManager modeManager,GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        var player = this.getPlayer();
        var team = teamManager.getPlayerTeam(player.getUniqueId());

        var host = gameManager.getHost();
        var uhcType = modeManager.getModesActive(ModalityType.UHC).stream().findFirst();
        var ranks = PlayerRankProfileRequestListener.playerRanksHashMap.get(player);
        var online = Bukkit.getOnlinePlayers().stream().filter(p -> p.getGameMode() != GameMode.SPECTATOR).toList().size();

        this.updateLines(
                         ChatUtils.format("&7&m--------------------"),
                         ranks != null ? String.format(ChatUtils.format("&7Prefixes: %s%s"), ranks.getStaffRank().getPrefix(), ranks.getDonatorRank().getPrefix()) : null,
                         ranks != null ? " " : null,
                         ChatUtils.format("&7Players: &f" + online),
                         ChatUtils.format("&7Host: &f" + (host != null ? host.getName() : "No hay host")),
                         ChatUtils.format("&7Uhc Type: &f" + (uhcType.isPresent() ? uhcType.get().getName() : "&cUhc Vanilla")),
                         " ",
                         ChatUtils.format("&7Border: &f" + gameManager.getWorldBorder()),
                         ChatUtils.format("&7Teams: &f" + teamManager.getFormatTeamSize()),
                         team != null ? " " : null,
                         team != null ? ChatUtils.format("&7Team Owner: " + team.getColor() + team.getOwner().getName()) : null,
                         modeManager.isActiveMode("team_inventory") && team != null ? ChatUtils.format("&7TI Size: &f" + gameManager.getTiSize() + " Slots") : null,
                         ChatUtils.format("&7&m--------------------")
        );
    }
}
