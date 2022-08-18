package io.github.wickeddroidmx.plugin.events.game;

import io.github.wickeddroidmx.plugin.game.GameManager;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameStartEvent extends Event {

    private final GameManager gameManager;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public GameStartEvent(GameManager gameManager){
        this.gameManager = gameManager;
    }

    public GameManager getGameManager() {
        return gameManager;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
