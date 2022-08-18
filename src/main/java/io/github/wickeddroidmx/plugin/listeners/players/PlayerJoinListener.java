package io.github.wickeddroidmx.plugin.listeners.players;

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
import org.bukkit.GameMode;
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

            cache.add(player.getUniqueId(), new LobbyScoreboard(player, modeManager, gameManager, playerManager, teamManager));
        } else if (gameManager.getGameState() != GameState.WAITING || gameManager.getGameState() != GameState.FINISHING) {
            if (cache.exists(player.getUniqueId()))
                return;

            cache.add(player.getUniqueId(), new GameScoreboard(player, modeManager, gameManager, playerManager, teamManager));
        } else {
            if (cache.exists(player.getUniqueId()))
                return;

            cache.add(player.getUniqueId(), new KillTopScoreboard(player, modeManager, gameManager, playerManager, teamManager));
        }
    }
}
