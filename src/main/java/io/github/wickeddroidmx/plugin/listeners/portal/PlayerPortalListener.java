package io.github.wickeddroidmx.plugin.listeners.portal;

import io.github.wickeddroidmx.plugin.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.inject.Inject;
import java.util.Random;

public class PlayerPortalListener implements Listener {

    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        Player player = e.getPlayer();

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            e.setCanCreatePortal(true);

            if (e.getFrom().getWorld().getEnvironment() == World.Environment.NETHER) {
                if (e.getFrom().getBlockX() * 8 > gameManager.getWorldBorder() || e.getFrom().getBlockZ() * 8 > gameManager.getWorldBorder()) {
                    e.setTo(new Location(Bukkit.getWorld("uhc_world"), new Random().nextInt(gameManager.getWorldBorder()), e.getFrom().getBlockY(), new Random().nextInt(gameManager.getWorldBorder())));

                    return;
                }

                e.setTo(new Location(Bukkit.getWorld("uhc_world"), e.getFrom().getBlockX() * 8, e.getFrom().getBlockY(), e.getFrom().getBlockZ() * 8));
            } else if (e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL) {
                e.setTo(new Location(Bukkit.getWorld("world_nether"), e.getFrom().getBlockX() / 8.0, e.getFrom().getBlockY(), e.getFrom().getBlockZ() / 8.0));
            }
        }
    }
}
