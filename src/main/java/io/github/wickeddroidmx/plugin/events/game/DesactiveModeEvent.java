package io.github.wickeddroidmx.plugin.events.game;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class DesactiveModeEvent extends Event {

    private final Modality modality;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public DesactiveModeEvent(Modality modality) {
        this.modality = modality;
    }

    public Modality getModality() {
        return modality;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
