package io.github.wickeddroidmx.plugin.sql;

public enum StatsUser {
    NAME("name"),
    KILLS("kills"),
    WINS("wins"),
    IRON_MAN("ironman");

    private final String convert;

    StatsUser(String convert) {
        this.convert = convert;
    }

    public String getConvert() {
        return convert;
    }
}
