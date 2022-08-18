package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.scoreboard.GameScoreboard;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
import io.github.wickeddroidmx.plugin.sql.SQLConsults;
import io.github.wickeddroidmx.plugin.sql.model.User;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

public class PlayerQuitListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private SQLConsults sqlConsults;

    @Inject
    @Named("scoreboard-cache")
    private MapCache<UUID, UHCScoreboard> cache;

    @Inject
    @Named("user-cache")
    private MapCache<UUID, User> userCache;

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        var player = e.getPlayer();
        var user = userCache.get(player.getUniqueId());

        e.quitMessage(
                Component
                        .text(ChatUtils.format(String.format("&7[&c-&7] %s", player.getName())))
        );

        if (cache.exists(player.getUniqueId())) {
            var scoreboard = cache.remove(player.getUniqueId());

            if (scoreboard != null)
                scoreboard.delete();
        }

        if (userCache.exists(player.getUniqueId()))
            sqlConsults.updateUser(player.getUniqueId().toString(), user.getWins(), user.getKills(), user.getIronMans());
    }
}
