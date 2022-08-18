package io.github.wickeddroidmx.plugin.listeners.worldborder;

import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderSetEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class WorldBorderSetListener implements Listener {

    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onWorldBorderSet(WorldBorderSetEvent e) {
        var world = Bukkit.getWorld("uhc_world");

        var worldBorder = e.getWorldBorder();
        var player = e.getPlayer();

        if (world != null) {
            world.getWorldBorder().setSize(worldBorder * 2);
            gameManager.setWorldBorder(worldBorder);

            if (player != null)
                player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("El borde se ha movido a &6%d&7x&6%d", worldBorder, worldBorder)));
        }
    }
}
