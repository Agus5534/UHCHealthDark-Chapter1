package io.github.wickeddroidmx.plugin.scoreboard;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class KillTopScoreboard extends UHCScoreboard {
    public KillTopScoreboard(Main plugin, Player player, ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        super(plugin, player, modeManager, gameManager, playerManager, teamManager);
    }

    @Override
    public void update(Main plugin, ModeManager modeManager, GameManager gameManager, PlayerManager playerManager, TeamManager teamManager) {
        var result = new LinkedHashMap<UUID, Integer>();

        var kt = new ArrayList<String>();

        playerManager.getKillTop().entrySet().stream().sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())).limit(10)
                .forEachOrdered(x -> result.put(x.getKey(), x.getValue()));


        this.updateTitle(ChatUtils.format("&7Top Kills"));

        for (var entry : result.entrySet()) {
            kt.add(ChatUtils.format(String.format("&7»" + teamManager.getPlayerTeam(entry.getKey()).getTeam().getColor() + " %s: &6%d", Bukkit.getOfflinePlayer(entry.getKey()).getName(), entry.getValue())));
        }

        this.updateLines(kt.size() > 0 ? kt : new ArrayList<>(Collections.singleton(ChatUtils.format("&7» No hubo kills"))));
    }
}
