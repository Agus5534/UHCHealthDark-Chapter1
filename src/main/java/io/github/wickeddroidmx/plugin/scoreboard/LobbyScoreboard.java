package io.github.wickeddroidmx.plugin.scoreboard;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class LobbyScoreboard extends UHCScoreboard {

    public LobbyScoreboard(Player player, ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        super(player, modeManager, gameManager, playerManager, teamManager);
    }

    @Override
    public void update(ModeManager modeManager,GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        var host = gameManager.getHost();
        var uhcType = modeManager.getModesActive(ModalityType.UHC).stream().findFirst();

        this.updateLines(
                         ChatUtils.format("&7&m--------------------"),
                         ChatUtils.format("&7Players: &f" + Bukkit.getOnlinePlayers().size()),
                         ChatUtils.format("&7Host: &f" + (host != null ? host.getName() : "No hay host")),
                         ChatUtils.format("&7Uhc Type: &f" + (uhcType.isPresent() ? uhcType.get().getName() : "&cUhc Vanilla")),
                         " ",
                         ChatUtils.format("&7Border: &f" + gameManager.getWorldBorder()),
                         ChatUtils.format("&7Teams: &f" + teamManager.getFormatTeamSize()),
                         ChatUtils.format("&7&m--------------------"));
    }
}
