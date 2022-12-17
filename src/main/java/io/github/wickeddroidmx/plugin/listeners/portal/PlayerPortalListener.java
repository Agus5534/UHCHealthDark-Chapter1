package io.github.wickeddroidmx.plugin.listeners.portal;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.Main;
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
import java.util.HashMap;
import java.util.Observable;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerPortalListener implements Listener {

    @Inject
    private GameManager gameManager;
    @Inject
    private Main plugin;
    private HashMap<Location, Double> portalLocationMultiplier;


    public PlayerPortalListener() {
        portalLocationMultiplier = new HashMap<>();
    }

    @EventHandler
    public void onPlayerPortal(PlayerPortalEvent e) {
        Player player = e.getPlayer();
        e.setCanCreatePortal(true);

        if(e.getCause() == PlayerTeleportEvent.TeleportCause.NETHER_PORTAL) {
            if(e.getFrom().getWorld().getEnvironment() == World.Environment.NETHER) {
                var toLocation = e.getTo();
                var fromLocation = e.getFrom();
                toLocation.setWorld(plugin.getWorldGenerator().getUhcWorld().getWorld());

                double multiplier = 8.0D;
                int fixes = 0;

                while ((isOutsideBorder(toLocation) || isOcean(toLocation)) && fixes < 400) {
                    fixes++;
                    multiplier = multiplier-multiplierRest(toLocation);
                    toLocation = fixLocation(fromLocation, multiplier);
                }

                e.setTo(toLocation);

                portalLocationMultiplier.put(toLocation, multiplier);
                return;
            }

            if(e.getFrom().getWorld().getEnvironment() == World.Environment.NORMAL) {
                var loc = e.getFrom();
                Location secondLoc = null;

                for(var locs : portalLocationMultiplier.keySet()) {
                    if(loc.distance(secondLoc) < 10) {
                        secondLoc = locs;
                    }
                }

                if(secondLoc != null) {
                    var multiplier = portalLocationMultiplier.get(secondLoc);
                    e.setTo(new Location(Bukkit.getWorld("world_nether"), e.getFrom().getBlockX() / multiplier, e.getFrom().getBlockY(), e.getFrom().getBlockZ() / multiplier));
                    return;
                }
            }

            e.setTo(new Location(Bukkit.getWorld("world_nether"), e.getFrom().getBlockX() / 8.0, e.getFrom().getBlockY(), e.getFrom().getBlockZ() / 8.0));
        }


        if(e.getCause() == PlayerTeleportEvent.TeleportCause.END_PORTAL) {
            if(e.getFrom().getWorld().getEnvironment() == World.Environment.THE_END) {
                var world = plugin.getWorldGenerator().getUhcWorld().getWorld();

                if(player.getBedSpawnLocation() != null) {
                    if(player.getBedSpawnLocation().getWorld().getName().equals(world.getName())) {
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
        var size = plugin.getWorldGenerator().getUhcWorld().getWorld().getWorldBorder().getSize()/2;
        double x = location.getX();
        double z = location.getZ();

        return ((x > size || (-x) > size) || (z > size || (-z) > size));
    }

    public boolean isOcean(Location location) {
        return location.getBlock().getBiome().toString().endsWith("OCEAN");
    }

    private Location fixLocation(Location location, double multiplier) {
        var newX = location.getX() * multiplier;
        var newZ = location.getZ() * multiplier;
        var y = ThreadLocalRandom.current().nextInt(6, 90);

        var newLoc = new Location(plugin.getWorldGenerator().getUhcWorld().getWorld(), newX, y, newZ);

        return newLoc;
    }

    private double multiplierRest(Location location) {
        var x = location.getX();
        var z = location.getZ();

        var size = plugin.getWorldGenerator().getUhcWorld().getWorld().getWorldBorder().getSize() / 2;

        if(isOutsideBorder(location)) {
            return 0.30D;
        }

        if(isOcean(location)) {
            return 0.14D;
        }

        return 0.21D;
    }
}