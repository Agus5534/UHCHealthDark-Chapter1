package io.github.wickeddroidmx.plugin.scoreboard;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.player.UhcPlayer;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.List;

public class GameScoreboard extends UHCScoreboard {

    public GameScoreboard(Main plugin, Player player, ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        super(plugin, player, modeManager,gameManager, playerManager, teamManager);
    }

    @Override
    public void update(Main plugin, ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        var player = this.getPlayer();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());
        var teamPlayer = teamManager.getPlayerTeam(player.getUniqueId());

        this.updateLines(
                ChatUtils.format("&7&m--------------------"),
                ChatUtils.format( "&7Time: &f" + formatTime(gameManager.getCurrentTime())),
                ChatUtils.format("&7 &f"),
                ChatUtils.format("&7Players Left: &f" + playerManager.getUhcPlayers().values().stream().filter(UhcPlayer::isAlive).filter(uhcPlayer1 -> !uhcPlayer1.isSpect()).toList().size()),
                ChatUtils.format("&7WorldBorder: &f" + Math.round(plugin.getWorldGenerator().getUhcWorld().getWorld().getWorldBorder().getSize()/2)),
                gameManager.isSkyHighMode() ? ChatUtils.format("&7Height: &fY"+gameManager.getCape()) : null,
                uhcPlayer != null || teamPlayer != null ? ChatUtils.format("&7 &f") : null,
                uhcPlayer != null ? ChatUtils.format("&7Kills: &f" + uhcPlayer.getKills()) : null,
                teamPlayer != null ? ChatUtils.format("&7Team Kills: &f" + teamPlayer.getKills()) : null,
                modeManager.isActiveMode("day_time") ? ChatUtils.format("&7Hora: " + dayTime(plugin)) : null,
                uhcPlayer != null && uhcPlayer.isExtraScoreStats() ? " " : null,
                uhcPlayer != null && teamPlayer != null && uhcPlayer.isExtraScoreStats() ? ChatUtils.format("&7Chat: &f" + (uhcPlayer.isChat() ? "&cEquipo" : "&bGlobal")) : null,
                uhcPlayer != null && uhcPlayer.isExtraScoreStats() ? ChatUtils.format(String.format("&7Coords: &fX%s Z%s", Math.round(player.getLocation().getX()), Math.round(player.getLocation().getZ()))) : null,
                uhcPlayer != null && teamPlayer != null && uhcPlayer.isExtraScoreStats() ? ChatUtils.format("&7Team Owner: &f"+getTeamOwner(teamPlayer, playerManager)) : null,
                ChatUtils.format("&7&m--------------------")
        );
    }

    private String formatTime(long totalSecs){
        int hours = (int) totalSecs / 3600;
        int minutes = (int) (totalSecs % 3600) / 60;
        int seconds = (int)     totalSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }

    private String dayTime(Main plugin) {
        long wTime = plugin.getWorldGenerator().getUhcWorld().getWorld().getTime();

        int hours = (int) ((wTime / 1000 + 8) % 24);
        int minutes = (int) (60 * (wTime % 1000) / 1000);

        String variation = (hours >= 0 && hours < 12 ? "AM" : "PM");

        return String.format("%02d:%02d %s", hours, minutes, variation);
    }

    private String getTeamOwner(UhcTeam uhcTeam, PlayerManager playerManager) {
        var player = uhcTeam.getOwner();
        var uhcPlayerOwner = playerManager.getPlayer(player.getUniqueId());

        if(uhcPlayerOwner == null) {
            return "Desconocido ☠";
        }

        if(!uhcPlayerOwner.isAlive()) {
            return player.getName() + " ☠";
        }

        if(!player.isOnline()) {
            return player.getName() + ChatColor.DARK_RED + " ❌";
        }

        return player.getName() + ChatColor.RED + " " + Math.round(player.getHealth()) + "❤";
    }
}
