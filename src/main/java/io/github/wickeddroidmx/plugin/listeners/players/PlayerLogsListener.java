package io.github.wickeddroidmx.plugin.listeners.players;

import com.destroystokyo.paper.event.player.PlayerStartSpectatingEntityEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerLogsListener implements Listener {

    @EventHandler
    public void onTeleport(PlayerTeleportEvent event) {
        if(event.getCause() == PlayerTeleportEvent.TeleportCause.SPECTATE ||
        event.getCause() == PlayerTeleportEvent.TeleportCause.UNKNOWN) {
            var player = event.getPlayer();

            var from = event.getFrom();
            var to = event.getTo();

            Bukkit.getLogger().info(String.format("Teleported player %s from %s (X: %d Y: %d Z: %d) to %s (X: %d Y: %d Z: %d). CAUSE: %s",
                    player.getName(),
                    from.getWorld().getName(),
                    Math.round(from.getX()),
                    Math.round(from.getY()),
                    Math.round(from.getZ()),
                    to.getWorld().getName(),
                    Math.round(to.getX()),
                    Math.round(to.getY()),
                    Math.round(to.getZ()),
                    event.getCause().toString()));
        }
    }

    @EventHandler
    public void onStartSpectating(PlayerStartSpectatingEntityEvent event) {
        var player = event.getPlayer();

        if(!(event.getNewSpectatorTarget() instanceof Player)) { return; }

        var entity = (Player) event.getNewSpectatorTarget();

        Bukkit.getLogger().info(String.format("[SPECT] %s started spectating %s",
                player.getName(),
                entity.getName()));
    }
}
