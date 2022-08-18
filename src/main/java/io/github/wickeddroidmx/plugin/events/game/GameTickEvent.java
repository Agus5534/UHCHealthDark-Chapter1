package io.github.wickeddroidmx.plugin.events.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class GameTickEvent extends Event {

    private final int time;
    private final int id;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public GameTickEvent(int time, int id, boolean async){
        super(async);

        this.time = time;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getTime() {
        return time;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
