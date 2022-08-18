package io.github.wickeddroidmx.plugin.listeners.team;

import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class TeamCreateListener implements Listener {

    @EventHandler
    public void onTeamCreate(TeamCreateEvent e) {
        var team = e.getUhcTeam();

        if (team != null)
            e.getPlayer().sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("Se ha creado el equipo &6%s &7correctamente.", e.getUhcTeam().getName())));
    }
}
