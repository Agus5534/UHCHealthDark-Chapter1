package io.github.wickeddroidmx.plugin.schedulers;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import me.yushust.inject.InjectAll;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import javax.inject.Singleton;

@InjectAll
@Singleton
public class GameTask extends BukkitRunnable {

    private Main plugin;
    private GameManager gameManager;

    @Override
    public void run() {
        if (gameManager.getGameState() != GameState.WAITING) {
            var gameTime = (int) (Math.floor((System.currentTimeMillis() - gameManager.getSeconds()) / 1000.0));

            gameManager.setCurrentTime(gameTime);

            Bukkit.getPluginManager().callEvent(new GameTickEvent(gameTime, getTaskId(), true));
        }
    }
}
