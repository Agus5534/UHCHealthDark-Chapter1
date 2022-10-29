package io.github.wickeddroidmx.plugin.listeners.team;

import io.github.wickeddroidmx.plugin.events.team.TeamFlagChangedEvent;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeamFlagChangedListener implements Listener {

    @EventHandler
    public void onFlagChange(TeamFlagChangedEvent event) {
        var team = event.getUhcTeam();
        var flag = event.getFlag();


        if(flag == TeamFlags.HIDE_TAB_NICKNAMES) {
            if(event.isNewValue()) {
                team.getTeam().setColor(ChatColor.MAGIC);
            } else {
                team.getTeam().setColor(team.getColor());
            }
        }

        if(flag == TeamFlags.FRIENDLY_FIRE) {
            team.setFriendlyFire(event.isNewValue());
        }
    }
}
