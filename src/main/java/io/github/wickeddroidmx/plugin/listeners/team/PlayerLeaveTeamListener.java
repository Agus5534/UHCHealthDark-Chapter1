package io.github.wickeddroidmx.plugin.listeners.team;

import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.PlayerLeaveTeamEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class PlayerLeaveTeamListener implements Listener {

    @EventHandler
    public void onPlayerLeaveTeam(PlayerLeaveTeamEvent e) {
        var uhcTeam = e.getUhcTeam();
        var playerLeave = e.getPlayerLeave();

        uhcTeam.getTeamPlayers()
                .stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .filter(OfflinePlayer::isOnline)
                .forEach(player -> player.sendMessage(ChatUtils.TEAM + ChatUtils.format(String.format("El usuario %s se ha salido del equipo", playerLeave.getName()))));

        uhcTeam.leavePlayer(playerLeave);

        if (playerLeave != null)
            playerLeave.sendMessage(ChatUtils.TEAM + ChatUtils.format(String.format("Te has salido del equipo de &6%s", uhcTeam.getOwner().getName())));
    }
}
