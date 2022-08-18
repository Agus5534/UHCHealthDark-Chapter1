package io.github.wickeddroidmx.plugin.teams;

import org.bukkit.entity.Player;

public class TeamInvite {

    private Player owner;
    private Player received;

    private final UhcTeam uhcTeam;

    public TeamInvite(Player owner, Player received, UhcTeam uhcTeam) {
        this.owner = owner;
        this.received = received;
        this.uhcTeam = uhcTeam;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setReceived(Player received) {
        this.received = received;
    }

    public Player getOwner() {
        return owner;
    }

    public UhcTeam getUhcTeam() {
        return uhcTeam;
    }

    public Player getReceived() {
        return received;
    }
}
