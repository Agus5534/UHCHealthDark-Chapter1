package io.github.wickeddroidmx.plugin.player;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class UhcInventory {
    private final Player player;
    private HashMap<Integer, ItemStack> inventory;
    private Location location;

    public UhcInventory(Player player) {
        this.player = player;
        this.inventory = new HashMap<>();
        this.location = null;
    }

    public Player getPlayer() {
        return player;
    }

    public HashMap<Integer, ItemStack> getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inv) {
        for(int i = 0; i < 36; i++) {
            inventory.put(i, inv.getItem(i));
        }

        for(int i = 36; i < 41; i++) {
            inventory.put(i, inv.getItem(i));
        }

    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
