package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

import javax.inject.Inject;

public class FoodLevelChangeListener implements Listener {

    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent e) {
        if (gameManager.getGameState() == GameState.WAITING) {
            e.setCancelled(true);
        }
    }
}
