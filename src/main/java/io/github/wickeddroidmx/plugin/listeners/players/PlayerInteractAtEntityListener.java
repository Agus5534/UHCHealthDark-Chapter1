package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.wickeddroidmx.plugin.menu.PlayerInventoryMenu;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import org.bukkit.GameMode;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.inventory.EquipmentSlot;

import javax.inject.Inject;

public class PlayerInteractAtEntityListener implements Listener {

    @Inject
    private PlayerManager playerManager;

    @EventHandler
    public void onInteract(PlayerInteractAtEntityEvent event) {
        if(event.getRightClicked().getType() != EntityType.PLAYER) {
            return;
        }

        if(event.getHand() != EquipmentSlot.HAND) { return; }

        var player = event.getPlayer();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());

        if(uhcPlayer == null) { return; }
        if(!uhcPlayer.isSpect()) { return; }
        if(player.getGameMode() != GameMode.SPECTATOR) { return; }

        new PlayerInventoryMenu((Player) event.getRightClicked()).openInv(player);
    }
}
