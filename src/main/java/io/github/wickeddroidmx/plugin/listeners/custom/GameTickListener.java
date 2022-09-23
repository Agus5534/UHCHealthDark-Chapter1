package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamWinEvent;
import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderMoveEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.scoreboard.KillTopScoreboard;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

public class GameTickListener implements Listener {

    @Inject
    private Main plugin;

    @Inject
    private TeamManager teamManager;

    @Inject
    private ModeManager modeManager;

    @Inject
    private PlayerManager playerManager;

    @Inject
    private GameManager gameManager;

    @Inject
    @Named("scoreboard-cache")
    private MapCache<UUID, UHCScoreboard> cache;

    @EventHandler
    public void onGameTick(GameTickEvent e) {
        var seconds = e.getTime();
        var uhcWorld = Bukkit.getWorld("uhc_world");

        if (gameManager.getGameState() == GameState.MEETUP) {
            if (teamManager.getCurrentTeams() == 1) {
                var lastTeam = teamManager.getUhcTeams().values().stream().findAny().orElse(null);

                if (lastTeam != null) {
                    if (gameManager.getTeamWin() == null) {
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new TeamWinEvent(lastTeam)));
                    }
                }
            }
        }

        if (seconds == gameManager.getTimeForPvP()) {
            Bukkit.getWorlds().forEach(world -> {
                world.setPVP(true);
            });

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendTitle(ChatUtils.format("&e⚠ &6PVP Activado &e⚠"), ChatUtils.format("&7El PvP se ha activado."), 20, 60, 20);

                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
            });
        }


        if (seconds == gameManager.getTimeForMeetup()) {
            gameManager.setGameState(GameState.MEETUP);

            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new WorldBorderMoveEvent(150, 300, false)));
        }

        if(seconds == gameManager.getTimeForMeetup() + 300 || seconds == gameManager.getTimeForMeetup() + 900 || seconds == gameManager.getTimeForMeetup() + 1200 || seconds == gameManager.getTimeForMeetup() + 1800) {
            var world = Bukkit.getWorld("uhc_world");

            int wb = (int) (world.getWorldBorder().getSize() / 2);

            Bukkit.getScheduler().runTask(plugin, ()->Bukkit.getPluginManager().callEvent(new WorldBorderMoveEvent(wb-35,300,false)));

        }

        if (gameManager.getGameState() == GameState.FINISHING) {
            cache.clear();

            Bukkit.getOnlinePlayers().forEach(player -> cache.add(player.getUniqueId(), new KillTopScoreboard(player, modeManager, gameManager, playerManager, teamManager)));
            Bukkit.getScheduler().cancelTask(e.getId());
        }

        if (uhcWorld != null)
            if (seconds >= gameManager.getTimeForMeetup())
                gameManager.setWorldBorder((int) uhcWorld.getWorldBorder().getSize() / 2);

    }
}
