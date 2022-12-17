package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.scoreboard.GameScoreboard;
import io.github.wickeddroidmx.plugin.scoreboard.KillTopScoreboard;
import io.github.wickeddroidmx.plugin.scoreboard.LobbyScoreboard;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
import io.github.wickeddroidmx.plugin.sql.SQLConsults;
import io.github.wickeddroidmx.plugin.sql.StatsUser;
import io.github.wickeddroidmx.plugin.sql.model.DefaultUser;
import io.github.wickeddroidmx.plugin.sql.model.User;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.yushust.inject.InjectAll;
import net.kyori.adventure.text.Component;
import net.kyori.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Statistic;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

@InjectAll
public class PlayerJoinListener implements Listener {

    private PlayerManager playerManager;
    private GameManager gameManager;
    private TeamManager teamManager;
    private ModeManager modeManager;
    private SQLConsults sqlConsults;
    private Main plugin;

    @Named("scoreboard-cache")
    private MapCache<UUID, UHCScoreboard> cache;

    @Named("user-cache")
    private MapCache<UUID, User> userCache;

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {


        var player = e.getPlayer();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());
        var uuid = player.getUniqueId().toString();
        var sqlPlayer = sqlConsults.getUser(uuid);

        if (!userCache.exists(player.getUniqueId()) && sqlPlayer == null) {
            userCache.add(player.getUniqueId(), new DefaultUser(uuid, player.getName()));
        }

        if(gameManager.getGameState() == GameState.WAITING && player.getWorld().equals(Bukkit.getWorlds().get(0))) {
            var world = player.getWorld();
            var advancements = Bukkit.getServer().advancementIterator();

            player.getInventory().clear();
            player.teleport(world.getSpawnLocation());
            player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(20);
            player.setHealth(20.0);
            player.setGameMode(Bukkit.getDefaultGameMode());
            player.setExp(0);
            player.setTotalExperience(0);
            player.setStatistic(Statistic.SLEEP_IN_BED,0);
            player.setStatistic(Statistic.KILL_ENTITY, EntityType.PLAYER, 0);
            player.setFlying(false);
            player.getActivePotionEffects().forEach(ef -> player.removePotionEffect(ef.getType()));

            while (advancements.hasNext()) {
                var progress = player.getAdvancementProgress(advancements.next());

                if(progress.isDone()) {
                    for(String s : progress.getAwardedCriteria()) {
                        progress.revokeCriteria(s);
                    }
                }
            }
        }

        if (!userCache.exists(player.getUniqueId()) && sqlPlayer != null) {
            userCache.add(player.getUniqueId(), new DefaultUser(uuid, player.getName(), sqlConsults.getUserStat(uuid, StatsUser.WINS), sqlConsults.getUserStat(uuid, StatsUser.KILLS), sqlConsults.getUserStat(uuid, StatsUser.IRON_MAN)));
        }

        if (gameManager.getGameState() != GameState.WAITING && uhcPlayer == null) {
            playerManager.createPlayer(player, false);

            player.setGameMode(GameMode.SPECTATOR);
            gameManager.getSpectatorTeam().addEntry(player.getName());
        }

        e.joinMessage(
                Component
                        .text(ChatUtils.format(String.format("&7[&a+&7] %s", player.getName())))
        );

        if (gameManager.getGameState() == GameState.WAITING) {
            if (cache.exists(player.getUniqueId()))
                return;

            cache.add(player.getUniqueId(), new LobbyScoreboard(plugin, player, modeManager, gameManager, playerManager, teamManager));
        } else if (gameManager.getGameState() != GameState.WAITING || gameManager.getGameState() != GameState.FINISHING) {
            if (cache.exists(player.getUniqueId()))
                return;

            cache.add(player.getUniqueId(), new GameScoreboard(plugin, player, modeManager, gameManager, playerManager, teamManager));
        } else {
            if (cache.exists(player.getUniqueId()))
                return;

            cache.add(player.getUniqueId(), new KillTopScoreboard(plugin, player, modeManager, gameManager, playerManager, teamManager));
        }
    }
}
