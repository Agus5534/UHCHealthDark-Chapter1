package io.github.wickeddroidmx.plugin.events.team;

import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinedTeamEvent extends Event {

    private static final HandlerList HANDLER_LIST = new HandlerList();
    private final UhcTeam uhcTeam;
    private final Player playerJoined;

    public PlayerJoinedTeamEvent(UhcTeam uhcTeam, Player playerJoined) {
        this.uhcTeam = uhcTeam;
        this.playerJoined = playerJoined;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }

    public Player getPlayerJoined() {
        return playerJoined;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
