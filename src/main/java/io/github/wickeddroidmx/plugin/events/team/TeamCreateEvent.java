package io.github.wickeddroidmx.plugin.events.team;

import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class TeamCreateEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final UhcTeam uhcTeam;

    private final Player player;

    public TeamCreateEvent(UhcTeam uhcTeam, Player player) {
        this.uhcTeam = uhcTeam;
        this.player = player;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
