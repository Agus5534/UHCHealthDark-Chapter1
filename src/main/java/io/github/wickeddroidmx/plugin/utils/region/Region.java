package io.github.wickeddroidmx.plugin.utils.region;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;
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

    public List<Block> getBlocksTypeOf(Material m) {
        List<Block> list = new ArrayList<>();

        for(int x = (int) minX; x < maxX; x++) {
            for(int z = (int) minZ; z < maxZ; z++) {
                for(int y = (int) minY; y < maxY; y++) {
                    Block block = firstPoint.getWorld().getBlockAt(x, y, z);

                    if(block.getType() == m) {
                        list.add(block);
                    }
                }
            }
        }

        return list;
    }
}
