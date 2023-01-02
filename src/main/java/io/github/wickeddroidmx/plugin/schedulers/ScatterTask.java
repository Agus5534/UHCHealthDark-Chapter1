package io.github.wickeddroidmx.plugin.schedulers;

import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.events.team.PlayerLeaveTeamEvent;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ScatterTask extends BukkitRunnable {

    private UhcTeam uhcTeam;
    private Location location;

    private Player player;

    public ScatterTask(UhcTeam uhcTeam, Location location) {
        this.uhcTeam = uhcTeam;
        this.location = location;
    }

    public ScatterTask(Player player, Location location) {
        this.uhcTeam = null;
        this.player = player;
        this.location = location;
    }

    @Override
    public void run() {
        if(uhcTeam == null) {

            if (player != null) {
                if (!player.isOnline()) {
                   return;
                }

                Bukkit.getPluginManager().callEvent(new PlayerScatteredEvent(player, location));
            }
            Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format(String.format("El jugador %s ha sido scatterado", player.getName()))));

            return;
        }

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

    }
}
