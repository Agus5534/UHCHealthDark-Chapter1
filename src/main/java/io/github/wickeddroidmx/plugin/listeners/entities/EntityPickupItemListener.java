package io.github.wickeddroidmx.plugin.listeners.entities;

import io.github.wickeddroidmx.plugin.game.GameManager;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;

import javax.inject.Inject;

public class EntityPickupItemListener implements Listener {
    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }

        if(!event.getItem().getType().equals(EntityType.DROPPED_ITEM)) { return; }

        if(event.getItem().getItemStack().getType() != Material.COBWEB) { return; }

        var player = (Player) event.getEntity();

        if(player.getInventory().contains(Material.COBWEB, gameManager.getCobwebLimit())) {
            event.setCancelled(true);
        }
    }
}
