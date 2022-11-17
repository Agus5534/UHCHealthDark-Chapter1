package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.agus5534.hdbot.minecraft.events.ThreadMessageLogEvent;
import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderSetEvent;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.hooks.DiscordWebhook;
import io.github.wickeddroidmx.plugin.hooks.discord.HookType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.schedulers.GameTask;
import io.github.wickeddroidmx.plugin.scoreboard.GameScoreboard;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.yushust.inject.InjectAll;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import javax.inject.Named;
import java.awt.*;
import java.io.IOException;
import java.util.UUID;

@InjectAll
public class GameStartListener implements Listener {

    private Main plugin;
    private GameTask gameScheduler;
    private ModeManager modeManager;
    private PlayerManager playerManager;
    private TeamManager teamManager;

    @Named("scoreboard-cache")
    private MapCache<UUID, UHCScoreboard> cache;

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        var gameManager = e.getGameManager();
        var uhcWorld = Bukkit.getWorld("uhc_world");

        cache.clear();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.getActivePotionEffects().forEach(
                    potionEffect -> player.removePotionEffect(potionEffect.getType())
            );

            player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 9, false, false, false));
            player.sendTitle(ChatUtils.format("&6¡UHC Iniciado!"), ChatUtils.format("&7El UHC ha iniciado"), 20, 60, 20);
            cache.add(player.getUniqueId(), new GameScoreboard(player, modeManager ,gameManager, playerManager, teamManager));
        });

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()-> {
            Bukkit.getPluginManager().callEvent(
                    new ThreadMessageLogEvent(
                            "> La partida ha comenzado",
                            gameManager.getUhcId()
                    )
            );
        }, 5L);

        var hook = new DiscordWebhook(HookType.LOGS.getUrl());

        hook.setUsername("Partida");
        hook.addEmbed(
                new DiscordWebhook.EmbedObject()
                        .setTitle("La partida ha iniciado")
                        .setColor(Color.YELLOW)
        );

        try {
            hook.execute();
        } catch (IOException err) {
            err.printStackTrace();
        }

        gameManager.setGameState(GameState.PLAYING);
        gameManager.setSeconds(System.currentTimeMillis());
        gameManager.setScatteredPlayers(false);

        if (uhcWorld != null) {
            var worldBorder = uhcWorld.getWorldBorder();

            Bukkit.getPluginManager().callEvent(new WorldBorderSetEvent(2000));
        }

        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.DO_MOB_SPAWNING, true);
            world.setGameRule(GameRule.DO_DAYLIGHT_CYCLE, true);
            world.setGameRule(GameRule.DO_WEATHER_CYCLE, true);

            world.setTime(0);
        });

        gameScheduler.runTaskTimerAsynchronously(plugin, 0L, 20L);
    }


}
