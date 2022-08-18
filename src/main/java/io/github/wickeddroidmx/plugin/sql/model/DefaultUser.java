package io.github.wickeddroidmx.plugin.sql.model;

public class DefaultUser implements User {

    private final String uuid;
    private String name;
    private int wins;
    private int kills;
    private int ironMans;

    public DefaultUser(String uuid, String name, int wins, int kills, int ironMans) {
        this.uuid = uuid;
        this.name = name;
        this.wins = wins;
        this.kills = kills;
        this.ironMans = ironMans;
    }

    public DefaultUser(String uuid, String name) {
        this(uuid, name, 0, 0, 0);
    }

    @Override
    public String getUuid() {
        return uuid;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void incrementIronMans() {
        ++this.ironMans;
    }

    @Override
    public void incrementWins() {
        ++this.wins;
    }

    @Override
    public void incrementKills() {
        ++this.kills;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int getWins() {
        return wins;
    }

    @Override
    public void setWins(int wins) {
        this.wins = wins;
    }

    @Override
    public int getKills() {
        return kills;
    }

    @Override
    public void setKills(int kills) {
        this.kills = kills;
    }

    @Override
    public int getIronMans() {
        return ironMans;
    }

    @Override
    public void setIronMans(int ironMans) {
        this.ironMans = ironMans;
    }
}
