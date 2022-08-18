package io.github.wickeddroidmx.plugin.schedulers;

import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.events.team.PlayerLeaveTeamEvent;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.awt.*;

public class ScatterTask extends BukkitRunnable {

    private UhcTeam uhcTeam;
    private Location location;

    public ScatterTask(UhcTeam uhcTeam, Location location) {
        this.uhcTeam = uhcTeam;
        this.location = location;
    }

    @Override
    public void run() {
        for (var uuid : uhcTeam.getTeamPlayers()) {
            var player = Bukkit.getPlayer(uuid);

            if (player != null) {
                if (!player.isOnline()) {
                    Bukkit.getPluginManager().callEvent(new PlayerLeaveTeamEvent(uhcTeam, player));
                    continue;
                }

                Bukkit.getPluginManager().callEvent(new PlayerScatteredEvent(player, location));
            }
        }

        Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format(String.format("El equipo %s ha sido scatterado", uhcTeam.getName()))));
    }
}
