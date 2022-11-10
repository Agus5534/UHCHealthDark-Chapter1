package io.github.wickeddroidmx.plugin.teams;

import io.github.wickeddroidmx.plugin.events.team.TeamFlagChangedEvent;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Team;

import java.util.function.Predicate;

public enum TeamFlags {
    BLOCK_NAME_CHANGE("blockNameChange", false),
    BLOCK_PREFIX_CHANGE("blockPrefixChange", false),
    BLOCK_PREFIX_SPECIAL_CHARACTERS("blockPrefixSpecialCharacters", true),
    BLOCK_TEAM_CHAT("blockTeamChat", false),
    BLOCK_COLOR_CHANGE("blockColorChange", false),
    BLOCK_TEAM_LOCATION("blockTeamLocation", false),
    ANYONE_CAN_MODIFY("anyoneCanModify", true),
    COLORED_OWNER_TEAMCHAT("coloredOwnerTeamChat", true),
    HIDE_TAB_NICKNAMES("hideTabNickNames", false, event -> {
        var team = event.getUhcTeam();

        if(event.isNewValue()) {
            team.getTeam().setColor(ChatColor.MAGIC);
        } else {
            team.getTeam().setColor(team.getColor());
        }

        return true;
    }),
    FRIENDLY_FIRE("friendlyFire", false, event -> {
        var team = event.getUhcTeam();

        team.setFriendlyFire(event.isNewValue());

        return true;
    }),
    HIDE_NICKNAMES("hideNickNames", false, event -> {
        var team = event.getUhcTeam();

        var optStatus = (event.isNewValue() ? Team.OptionStatus.FOR_OTHER_TEAMS : Team.OptionStatus.ALWAYS);
        team.getTeam().setOption(Team.Option.NAME_TAG_VISIBILITY, optStatus);
        return true;
    });

    private final String name;
    private final boolean defaultEnabled;
    private final Predicate<TeamFlagChangedEvent> predicate;

    TeamFlags(String name, boolean defaultEnabled) {
        this.name = name;
        this.defaultEnabled = defaultEnabled;
        predicate = e -> true;
    }

    TeamFlags(String name, boolean defaultEnabled, Predicate<TeamFlagChangedEvent> predicate) {
        this.name = name;
        this.defaultEnabled = defaultEnabled;
        this.predicate = predicate;
    }

    public String getName() {
        return name;
    }

    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }

    public Predicate<TeamFlagChangedEvent> getPredicate() {
        return predicate;
    }
}

