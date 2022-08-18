package io.github.wickeddroidmx.plugin.events.team;

import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerPromotedTeamEvent extends Event {

    public static final HandlerList HANDLER_LIST = new HandlerList();

    private final UhcTeam uhcTeam;
    private final Player playerPromoted;

    public PlayerPromotedTeamEvent(UhcTeam uhcTeam, Player playerPromoted) {
        this.uhcTeam = uhcTeam;
        this.playerPromoted = playerPromoted;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }

    public Player getPlayerPromoted() {
        return playerPromoted;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
