package io.github.wickeddroidmx.plugin.listeners.team;

import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.Objects;

public class PlayerJoinedTeamListener implements Listener {

    @EventHandler
    public void onPlayerJoinedTeam(PlayerJoinedTeamEvent e) {
        var uhcTeam = e.getUhcTeam();
        var playerJoined = e.getPlayerJoined();

        uhcTeam.getTeamPlayers()
                .stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .filter(OfflinePlayer::isOnline)
                .forEach(player -> player.sendMessage(ChatUtils.TEAM + ChatUtils.format(String.format("El usuario %s se ha unido al equipo", playerJoined.getName()))));

        uhcTeam.addPlayer(playerJoined);

        playerJoined.sendMessage(ChatUtils.TEAM + ChatUtils.format(String.format("Te has unido al equipo de &6%s", uhcTeam.getOwner().getName())));
    }
}
