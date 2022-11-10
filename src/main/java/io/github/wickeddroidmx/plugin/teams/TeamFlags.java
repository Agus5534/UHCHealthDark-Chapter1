package io.github.wickeddroidmx.plugin.teams;

public enum TeamFlags {
    BLOCK_NAME_CHANGE("blockNameChange", false),
    BLOCK_PREFIX_CHANGE("blockPrefixChange", false),
    HIDE_NICKNAMES("hideNickNames", false),
    HIDE_TAB_NICKNAMES("hideTabNickNames", false),
    FRIENDLY_FIRE("friendlyFire", false),
    BLOCK_PREFIX_SPECIAL_CHARACTERS("blockPrefixSpecialCharacters", true),
    BLOCK_TEAM_CHAT("blockTeamChat", false);

    private final String name;
    private final boolean defaultEnabled;

    TeamFlags(String name, boolean defaultEnabled) {
        this.name = name;
        this.defaultEnabled = defaultEnabled;
    }

    public String getName() {
        return name;
    }

    public boolean isDefaultEnabled() {
        return defaultEnabled;
    }
}

