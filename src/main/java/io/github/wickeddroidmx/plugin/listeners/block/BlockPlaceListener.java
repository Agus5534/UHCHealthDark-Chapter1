package io.github.wickeddroidmx.plugin.listeners.block;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import javax.inject.Inject;

public class BlockPlaceListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerManager playerManager;

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());
        var cobwebLimit = gameManager.getCobwebLimit();

        if(uhcPlayer == null) { return; }

        if(gameManager.getGameState() == GameState.WAITING) { return; }

        if(event.getBlockPlaced().getType() != Material.COBWEB) { return; }

        if(uhcPlayer.getCobwebs() > cobwebLimit) {
            event.setCancelled(true);
            return;
        }

        uhcPlayer.incrementCobwebs();
    }
}
