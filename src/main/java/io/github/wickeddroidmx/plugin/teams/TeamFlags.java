package io.github.wickeddroidmx.plugin.teams;

public enum TeamFlags {
    BLOCK_NAME_CHANGE("blockNameChange"),
    BLOCK_PREFIX_CHANGE("blockPrefixChange"),
    HIDE_NICKNAMES("hideNickNames"),
    HIDE_TAB_NICKNAMES("hideTabNickNames"),
    FRIENDLY_FIRE("friendlyFire"),
    BLOCK_PREFIX_SPECIAL_CHARACTERS("blockPrefixSpecialCharacters"),

    BLOCK_TEAM_CHAT("blockTeamChat");

    private final String name;

    TeamFlags(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

