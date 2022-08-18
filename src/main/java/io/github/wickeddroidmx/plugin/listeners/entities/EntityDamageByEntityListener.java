package io.github.wickeddroidmx.plugin.listeners.entities;

import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;

public class EntityDamageByEntityListener implements Listener {

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getDamager() instanceof Player player && e.getEntity() instanceof Player victim) {
            if (victim.isBlocking() && isAxe(player.getInventory().getItemInMainHand())) {
                player.playSound(player.getLocation(), Sound.ITEM_SHIELD_BREAK, 1.0F, 1.0F);
            }
        }
    }

    public boolean isAxe(ItemStack itemStack) {
        return itemStack.getType() == Material.WOODEN_AXE ||
                itemStack.getType() == Material.STONE_AXE ||
                itemStack.getType() == Material.IRON_AXE ||
                itemStack.getType() == Material.GOLDEN_AXE ||
                itemStack.getType() == Material.DIAMOND_AXE ||
                itemStack.getType() == Material.NETHERITE_AXE;
    }
}
