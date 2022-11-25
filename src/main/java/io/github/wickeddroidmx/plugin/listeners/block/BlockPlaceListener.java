package io.github.wickeddroidmx.plugin.listeners.block;

import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
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

    @Inject
    private ExperimentManager experimentManager;

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        var player = event.getPlayer();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());
        var cobwebLimit = gameManager.getCobwebLimit()*2;

        if(uhcPlayer == null) { return; }

        if(gameManager.getGameState() == GameState.WAITING) { return; }

        if(event.getBlockPlaced().getType() != Material.COBWEB) { return; }

        int cobwebsLeft = cobwebLimit-uhcPlayer.getCobwebs();

        if(experimentManager.hasExperiment(player, "COBWEB_WARN_EXPERIMENT")) {
            var msg = ChatUtils.formatC(String.format("&7Te quedan &c%s &7cobwebs por colocar", cobwebsLeft));

            player.sendActionBar(msg);
        }


        if(uhcPlayer.getCobwebs() > cobwebLimit) {
            event.setCancelled(true);
            return;
        }

        uhcPlayer.incrementCobwebs();
    }
}
