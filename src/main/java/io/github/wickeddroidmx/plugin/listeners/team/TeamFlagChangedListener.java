package io.github.wickeddroidmx.plugin.listeners.team;

import io.github.wickeddroidmx.plugin.events.team.TeamFlagChangedEvent;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.Team;

public class TeamFlagChangedListener implements Listener {

    @EventHandler
    public void onFlagChange(TeamFlagChangedEvent event) {
        var team = event.getUhcTeam();
        var flag = event.getFlag();

        flag.getPredicate().test(event);
    }
}
