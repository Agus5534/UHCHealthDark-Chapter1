package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import javax.inject.Inject;

public class PlayerPreLoginListener implements Listener {

    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onAsyncPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        if (gameManager.isScatteredPlayers())
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_FULL, Component.text(ChatUtils.format("&6No puedes unirte en medio del scatter, intentalo más tarde.")));
    }
}
