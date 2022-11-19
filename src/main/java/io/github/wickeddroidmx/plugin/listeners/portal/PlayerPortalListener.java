package io.github.wickeddroidmx.plugin.listeners.portal;

import io.github.wickeddroidmx.plugin.game.GameManager;
import net.minecraft.world.level.levelgen.structure.WorldGenNetherPieces;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
        e.setCanCreatePortal(true);

        if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if(e.getFrom().getWorld().getEnvironment() == World.Environment.NETHER) {
                var toLocation = e.getTo();
                toLocation.setWorld(Bukkit.getWorld("uhc_world"));

                while (isOutsideBorder(toLocation)) {
                    toLocation = fixLocation(toLocation);
                }

                e.setTo(toLocation);
                return;
            }

            e.setTo(new Location(Bukkit.getWorld("world_nether"), e.getFrom().getBlockX() / 8.0, e.getFrom().getBlockY(), e.getFrom().getBlockZ() / 8.0));
        }


        if(e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            if(e.getFrom().getWorld().getEnvironment() == World.Environment.THE_END) {
                var world = Bukkit.getWorld("uhc_world");

                if(player.getBedSpawnLocation() != null) {
                    if(player.getBedSpawnLocation().getWorld().getName().equals("uhc_world")) {
                        e.setTo(player.getBedSpawnLocation());
                        return;
                    }
                }

                e.setTo(world.getSpawnLocation());
            }
        }

        var location = e.getTo();

        Bukkit.getLogger().info(String.format("The player %s changed dimension from %s to %s (X: %d Y: %d Z: %d)",
                e.getPlayer(),
                e.getFrom().getWorld().getName(),
                e.getTo().getWorld().getName(),
                Math.round(location.getX()),
                Math.round(location.getY()),
                Math.round(location.getZ())));
    }


    public boolean isOutsideBorder(Location location) {
        var size = Bukkit.getWorld("uhc_world").getWorldBorder().getSize();
        double x = location.getX();
        double z = location.getZ();

        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }

    private Location fixLocation(Location location) {
        var size = Bukkit.getWorld("uhc_world").getWorldBorder().getSize();
        double x = size / 2;
        double z = size / 2;

        double newX = (location.getX() < 0 ? (location.getX() < -x ? x+27 : location.getX()) : (location.getX() > x ? x-27 : location.getX()));
        double newZ = (location.getZ() < 0 ? (location.getZ() < -z ? z+27 : location.getZ()) : (location.getZ() > z ? z-27 : location.getZ()));

        var newLoc = new Location(location.getWorld(), newX, 16, newZ);
        newLoc.setY(newLoc.getWorld().getHighestBlockYAt(newLoc));

        return newLoc;
    }
}