package io.github.wickeddroidmx.plugin.events.team;

import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TeamWinEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final UhcTeam uhcTeam;

    public TeamWinEvent(UhcTeam uhcTeam) {
        this.uhcTeam = uhcTeam;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
