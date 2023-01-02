package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.agus5534.hdbot.minecraft.events.ThreadMessageLogEvent;
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
import org.bukkit.Bukkit;
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
        var uhcWorld = plugin.getWorldGenerator().getUhcWorld().getWorld();

        if (gameManager.getGameState() == GameState.MEETUP) {
            if (teamManager.getCurrentTeams() == 1 && !modeManager.isActiveMode("moles")) {
                var lastTeam = teamManager.getUhcTeams().values().stream().findAny().orElse(null);

                if (lastTeam != null) {
                    if (gameManager.getTeamWin() == null) {
                        Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new TeamWinEvent(lastTeam)));
                    }
                }
            }
        }

        if (seconds == gameManager.getTimeForPvP()) {
            Bukkit.getWorlds().forEach(world -> world.setPVP(true));

            Bukkit.getOnlinePlayers().forEach(player -> {
                player.sendTitle(ChatUtils.format("&e⚠ &6PVP Activado &e⚠"), ChatUtils.format("&7El PvP se ha activado."), 20, 60, 20);

                player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
            });

            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
                Bukkit.getPluginManager().callEvent(
                        new ThreadMessageLogEvent(
                                "> El PvP ha comenzado",
                                gameManager.getUhcId()
                        )
                );
            }, 5L);
        }


        if (seconds == gameManager.getTimeForMeetup()) {
            gameManager.setGameState(GameState.MEETUP);


            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
                Bukkit.getPluginManager().callEvent(
                        new ThreadMessageLogEvent(
                                "> El meetup ha comenzado",
                                gameManager.getUhcId()
                        )
                );
            }, 5L);
        }

        if(seconds == gameManager.getTimeWorldBorderOne()) {
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new WorldBorderMoveEvent(gameManager.getSizeWorldBorderOne(), gameManager.getBorderDelay(), false)));
        }

        if(seconds == gameManager.getTimeWorldBorderTwo()) {
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new WorldBorderMoveEvent(gameManager.getSizeWorldBorderTwo(), gameManager.getBorderDelay(), false)));
        }

        if(seconds == gameManager.getTimeWorldBorderThree()) {
            Bukkit.getScheduler().runTask(plugin, () -> Bukkit.getPluginManager().callEvent(new WorldBorderMoveEvent(gameManager.getSizeWorldBorderThree(), gameManager.getBorderDelay(), false)));
        }

        if (gameManager.getGameState() == GameState.FINISHING) {
            cache.clear();

            Bukkit.getOnlinePlayers().forEach(player -> cache.add(player.getUniqueId(), new KillTopScoreboard(plugin, player, modeManager, gameManager, playerManager, teamManager)));
            Bukkit.getScheduler().cancelTask(e.getId());
        }

        if (uhcWorld != null)
            if (seconds >= gameManager.getTimeForMeetup())
                gameManager.setWorldBorder((int) uhcWorld.getWorldBorder().getSize() / 2);

    }
}
