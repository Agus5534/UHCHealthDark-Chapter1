package io.github.wickeddroidmx.plugin.utils.region;

import org.bukkit.Location;

import java.util.UUID;

public class Region {

    private Location firstPoint, secondPoint;
    private UUID worldUUID;

    private double maxX, maxY, maxZ, minX, minY, minZ;

    public Region(Location firstPoint, Location secondPoint) {
        this.firstPoint = firstPoint;
        this.secondPoint = secondPoint;

        this.worldUUID = firstPoint.getWorld().getUID();

        maxX = Math.max(firstPoint.getX(), secondPoint.getX());
        minX = Math.min(firstPoint.getX(), secondPoint.getX());

        maxY = Math.max(firstPoint.getY(), secondPoint.getY());
        minY = Math.min(firstPoint.getY(), secondPoint.getY());

        maxZ = Math.max(firstPoint.getZ(), secondPoint.getZ());
        minZ = Math.min(firstPoint.getZ(), secondPoint.getZ());
    }

    public Location getFirstPoint() {
        return firstPoint;
    }

    public Location getSecondPoint() {
        return secondPoint;
    }

    public UUID getWorldUUID() {
        return worldUUID;
    }

    public boolean isInsideRegion(Location location) {
        return location.getWorld().getUID().equals(this.worldUUID)
                && location.getX() >= minX && location.getX() <= maxX
                && location.getY() >= minY && location.getY() <= maxY
                && location.getZ() >= minZ && location.getZ() <= maxZ;
    }
}
