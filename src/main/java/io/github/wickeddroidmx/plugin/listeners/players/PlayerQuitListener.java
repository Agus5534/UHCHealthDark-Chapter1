package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
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
    @Named("scoreboard-cache")
    private MapCache<UUID, UHCScoreboard> cache;

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        var player = e.getPlayer();

        e.quitMessage(
                Component
                        .text(ChatUtils.format(String.format("&7[&c-&7] %s", player.getName())))
        );

        if (cache.exists(player.getUniqueId())) {
            var scoreboard = cache.remove(player.getUniqueId());

            if (scoreboard != null)
                scoreboard.delete();
        }

        var location = player.getLocation();
        Bukkit.getLogger().info(String.format("%s has left at %s (X: %d Y: %d Z: %d)",
                player.getName(),
                location.getWorld().getName(),
                Math.round(location.getX()),
                Math.round(location.getY()),
                Math.round(location.getZ())));
    }
}
