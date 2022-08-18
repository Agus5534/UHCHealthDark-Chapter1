package io.github.wickeddroidmx.plugin.listeners.team;

import io.github.wickeddroidmx.plugin.events.team.TeamDeleteEvent;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class TeamDeleteListener implements Listener {

    @Inject
    private TeamManager teamManager;

    @EventHandler
    public void onTeamDelete(TeamDeleteEvent e) {
        var team = e.getUhcTeam();
        var owner = team.getOwner();

        teamManager.sendMessage(owner.getUniqueId(), "Se ha eliminado el equipo.");

        teamManager.removeTeam(owner);
    }
}
