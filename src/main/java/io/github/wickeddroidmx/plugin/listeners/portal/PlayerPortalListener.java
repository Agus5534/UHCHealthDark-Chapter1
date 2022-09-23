package io.github.wickeddroidmx.plugin.listeners.portal;

import io.github.wickeddroidmx.plugin.game.GameManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.data.type.Observer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import javax.inject.Inject;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerPortalListener implements Listener {

    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        Player player = e.getPlayer();

        if (e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            e.setCanCreatePortal(true);

            if (e.getFrom().getWorld().getEnvironment() == World.Environment.NETHER) {
                var loc = new Location(Bukkit.getWorld("uhc_world"), e.getFrom().getX() * 8, e.getFrom().getY(), e.getFrom().getZ() * 8);

                if(isOutsideBorder(loc)) {
                    int size = (int) Bukkit.getWorld("uhc_world").getWorldBorder().getSize();
                    var safeLoc = new Location(Bukkit.getWorld("uhc_world"), new Random(ThreadLocalRandom.current().nextInt(1500)).nextInt((size/2)-3), e.getFrom().getY(), new Random(ThreadLocalRandom.current().nextInt(1500)).nextInt((size/2)-3));
                    safeLoc.setY(safeLoc.getWorld().getHighestBlockYAt(safeLoc));

                    e.setTo(safeLoc);
                    return;
                }

                e.setTo(loc);
            } else if (e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL) {
                e.setTo(new Location(Bukkit.getWorld("world_nether"), e.getFrom().getBlockX() / 8.0, e.getFrom().getBlockY(), e.getFrom().getBlockZ() / 8.0));
            }
        }
    }


    public boolean isOutsideBorder(Location location) {
        var size = Bukkit.getWorld("uhc_world").getWorldBorder().getSize();
        double x = location.getX();
        double z = location.getZ();

        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }
}
