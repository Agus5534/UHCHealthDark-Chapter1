package io.github.wickeddroidmx.plugin.events.team;

import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import org.bukkit.Location;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TeamScatteredEvent extends Event {
    private static final HandlerList HANDLER_LIST = new HandlerList();

    private final UhcTeam uhcTeam;
    private final Location location;

    public TeamScatteredEvent(UhcTeam uhcTeam, Location location) {
        this.uhcTeam = uhcTeam;
        this.location = location;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }

    public Location getLocation() {
        return location;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }
}
