package io.github.wickeddroidmx.plugin.events.team;

import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveTeamEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final UhcTeam uhcTeam;
    private final Player playerLeave;

    public PlayerLeaveTeamEvent(UhcTeam uhcTeam, Player playerLeave) {
        this.uhcTeam = uhcTeam;
        this.playerLeave = playerLeave;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }

    public Player getPlayerLeave() {
        return playerLeave;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
