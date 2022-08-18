package io.github.wickeddroidmx.plugin.listeners.team;

import io.github.wickeddroidmx.plugin.events.team.PlayerPromotedTeamEvent;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class PlayerPromotedTeamListener implements Listener {

    @Inject
    private TeamManager teamManager;

    @EventHandler
    public void onPlayerPromotedTeam(PlayerPromotedTeamEvent e) {
        var player = e.getPlayerPromoted();
        var uhcTeam = e.getUhcTeam();

        uhcTeam.setOwner(player);
        teamManager.sendMessage(player.getUniqueId(), ChatUtils.format(String.format("El usuario %s ha sido promoteado.", player.getName())));
    }
}
