package io.github.wickeddroidmx.plugin.events.game;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class ChangeGameTimeEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final int timePvp,
                    timeMeetup;

    public ChangeGameTimeEvent(int timeMeetup, int timePvp) {
        this.timeMeetup = timeMeetup;
        this.timePvp = timePvp;
    }

    public int getMeetupTime() {
        return timeMeetup;
    }

    public int getPvpTime() {
        return timePvp;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
