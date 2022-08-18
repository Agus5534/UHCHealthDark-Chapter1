package io.github.wickeddroidmx.plugin.scoreboard;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.player.UhcPlayer;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.entity.Player;

import java.util.stream.Collectors;

public class GameScoreboard extends UHCScoreboard {

    public GameScoreboard(Player player, ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        super(player, modeManager,gameManager, playerManager, teamManager);
    }

    @Override
    public void update(ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        var player = this.getPlayer();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());
        var teamPlayer = teamManager.getPlayerTeam(player.getUniqueId());

        this.updateLines(
                ChatUtils.format("&7&m--------------------"),
                ChatUtils.format( "&7Time: &f" + formatTime(gameManager.getCurrentTime())),
                ChatUtils.format("&7 &f"),
                ChatUtils.format("&7Players Left: &f" + playerManager.getUhcPlayers().values().stream().filter(UhcPlayer::isAlive).toList().size()),
                ChatUtils.format("&7WorldBorder: &f" + gameManager.getWorldBorder()),
                ChatUtils.format("&7 &f"),
                ChatUtils.format("&7Kills: &f" + uhcPlayer.getKills()),
                ChatUtils.format("&7Team Kills: &f" + (teamPlayer != null ? teamPlayer.getKills() : uhcPlayer.getKills())),
                ChatUtils.format("&7&m--------------------")
                );
    }

    private String formatTime(long totalSecs){
        int hours = (int) totalSecs / 3600;
        int minutes = (int) (totalSecs % 3600) / 60;
        int seconds = (int)     totalSecs % 60;

        return String.format("%02d:%02d:%02d", hours, minutes, seconds);
    }
}
