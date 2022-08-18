package io.github.wickeddroidmx.plugin.listeners.block;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;

public class BlockBreakListener implements Listener {

    @Inject
    private PlayerManager playerManager;

    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var player = e.getPlayer();
        var block = e.getBlock();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());

        if (gameManager.getGameState() == GameState.WAITING && !player.isOp()) {
            e.setCancelled(true);
            return;
        }

        if (uhcPlayer != null && uhcPlayer.isCobbleOnly()) {
            if (block.getType() == Material.DEEPSLATE
                    || block.getType() == Material.DIORITE
                    || block.getType() == Material.ANDESITE
                    || block.getType() == Material.GRANITE) {
                e.setDropItems(false);

                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.COBBLESTONE));
            }
        }
    }
}
