package io.github.wickeddroidmx.plugin.events.team;

import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TeamFlagChangedEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final UhcTeam uhcTeam;
    private final TeamFlags flag;
    private final boolean newValue;

    public TeamFlagChangedEvent(UhcTeam uhcTeam, TeamFlags flag, boolean newValue) {
        this.uhcTeam = uhcTeam;
        this.flag = flag;
        this.newValue = newValue;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public TeamFlags getFlag() {
        return flag;
    }

    public boolean isNewValue() {
        return newValue;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }
}
