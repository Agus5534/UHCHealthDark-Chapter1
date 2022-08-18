package io.github.wickeddroidmx.plugin.events.worldborder;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class WorldBorderMoveEvent extends Event {

    private final int worldBorder;
    private final int seconds;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public WorldBorderMoveEvent(int worldBorder, int seconds, boolean async) {
        super(async);
        this.worldBorder = worldBorder;
        this.seconds = seconds;
    }

    public int getWorldBorder() {
        return worldBorder;
    }

    public int getSeconds() {
        return seconds;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
