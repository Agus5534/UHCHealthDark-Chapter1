package io.github.wickeddroidmx.plugin.sql.model;

public interface User {

    String getUuid();

    String getName();

    void incrementIronMans();

    void incrementWins();

    void incrementKills();

    void setName(String name);

    int getWins();

    void setWins(int wins);

    int getKills();

    void setKills(int kills);

    int getIronMans();

    void setIronMans(int ironMans);
}
