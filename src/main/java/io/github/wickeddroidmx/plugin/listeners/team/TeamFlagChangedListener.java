package io.github.wickeddroidmx.plugin.listeners.team;

import io.github.wickeddroidmx.plugin.events.team.TeamFlagChangedEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeamFlagChangedListener implements Listener {

    @EventHandler
    public void onFlagChange(TeamFlagChangedEvent event) {
        var team = event.getUhcTeam();
        var flag = event.getFlag();

        flag.getPredicate().test(event);
    }
}
