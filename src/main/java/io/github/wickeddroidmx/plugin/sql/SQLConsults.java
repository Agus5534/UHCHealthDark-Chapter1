package io.github.wickeddroidmx.plugin.sql;

import io.github.wickeddroidmx.plugin.connection.SQLClient;
import org.bukkit.Bukkit;
import org.jdbi.v3.core.Handle;
import org.jdbi.v3.core.Jdbi;

import javax.inject.Inject;
import javax.inject.Named;

public class SQLConsults {

    @Inject
    @Named("sql-user")
    private SQLClient sqlClient;

    public void createUser(String uuid, String name, int wins, int kills, int ironMan) {
        Jdbi connection = sqlClient.getConnection();

        if (userExists(uuid)) {
            Bukkit.getLogger().info("El usuario ya fue creado");
            return;
        }

        try (Handle handle = connection.open()) {
            handle.createUpdate("INSERT INTO `<TABLE>` (uuid, name, wins, kills, ironman) VALUES (?,?,?,?,?)")
                    .define("TABLE", "user")
                    .bind(0, uuid)
                    .bind(1, name)
                    .bind(2, wins)
                    .bind(3, kills)
                    .bind(4, ironMan)
                    .execute();
        }
    }

    public String getUser(String uuid) {
        Jdbi connection = sqlClient.getConnection();

        if (!userExists(uuid)) {
            Bukkit.getLogger().info("El usuario no existe.");
            return "XD";
        }

        try (Handle handle = connection.open()) {
            return handle.select("SELECT `name` FROM `<TABLE>` WHERE `uuid` = ?")
                    .define("TABLE", "user")
                    .bind(0, uuid)
                    .mapTo(String.class)
                    .first();
        }
    }

    public int getUserStat(String uuid, StatsUser statsUser) {
        Jdbi connection = sqlClient.getConnection();

        if (!userExists(uuid)) {
            Bukkit.getLogger().info("El usuario no existe.");
            return 0;
        }

        try (Handle handle = connection.open()) {
            return handle.select("SELECT `<TYPE>` FROM `<TABLE>` WHERE `uuid` = ?")
                    .define("TYPE", statsUser.getConvert())
                    .define("TABLE", "user")
                    .bind(0, uuid)
                    .mapTo(Integer.class)
                    .first();
        }
    }

    public void updateUser(String uuid, int wins, int kills, int ironMan) {
        Jdbi connection = sqlClient.getConnection();

        if (!userExists(uuid)) {
            Bukkit.getLogger().info("El usuario no existe.");
            return;
        }

        try (Handle handle = connection.open()) {
            handle.createUpdate("UPDATE `<TABLE>` SET `wins` = ?, `kills` = ?, `ironman` = ? WHERE `uuid` = ?")
                    .define("TABLE", "user")
                    .bind(0, wins)
                    .bind(1, kills)
                    .bind(2, ironMan)
                    .bind(3, uuid)
                    .execute();
        }
    }

    public void updateWins(String uuid, int wins) {
        Jdbi connection = sqlClient.getConnection();

        if (!userExists(uuid)) {
            Bukkit.getLogger().info("El usuario no existe.");
            return;
        }

        try (Handle handle = connection.open()) {
            handle.createUpdate("UPDATE `<TABLE>` SET `wins` = ? WHERE `uuid` = ?")
                    .define("TABLE", "user")
                    .bind(0, wins)
                    .bind(1, uuid)
                    .execute();
        }
    }

    public void updateKills(String uuid, int kills) {
        Jdbi connection = sqlClient.getConnection();

        if (!userExists(uuid)) {
            Bukkit.getLogger().info("El usuario no existe.");
            return;
        }

        try (Handle handle = connection.open()) {
            handle.createUpdate("UPDATE `<TABLE>` SET `kills` = ? WHERE `uuid` = ?")
                    .define("TABLE", "user")
                    .bind(0, kills)
                    .bind(1, uuid)
                    .execute();
        }
    }

    public void updateIronMan(String uuid, int ironMan) {
        Jdbi connection = sqlClient.getConnection();

        if (!userExists(uuid)) {
            Bukkit.getLogger().info("El usuario no existe.");
            return;
        }

        try (Handle handle = connection.open()) {
            handle.createUpdate("UPDATE `<TABLE>` SET `ironman` = ? WHERE `uuid` = ?")
                    .define("TABLE", "user")
                    .bind(0, ironMan)
                    .bind(1, uuid)
                    .execute();
        }
    }

    public void updateName(String uuid, String name) {
        Jdbi connection = sqlClient.getConnection();

        if (!userExists(uuid)) {
            Bukkit.getLogger().info("El usuario no existe.");
            return;
        }

        try (Handle handle = connection.open()) {
            handle.createUpdate("UPDATE `<TABLE>` SET `name` = ? WHERE `uuid` = ?")
                    .define("TABLE", "user")
                    .bind(0, name)
                    .bind(1, uuid)
                    .execute();
        }
    }

    public void deleteUser(String uuid) {
        Jdbi connection = sqlClient.getConnection();

        if (!userExists(uuid)) {
            Bukkit.getLogger().info("El usuario no existe.");
            return;
        }

        try (Handle handle = connection.open()) {
            handle.createUpdate("DELETE FROM `<TABLE>` WHERE `uuid` = ?")
                    .define("TABLE", "user")
                    .bind(0, uuid)
                    .execute();
        }
    }

    public boolean userExists(String uuid) {
        Jdbi connection = sqlClient.getConnection();

        try (Handle handle = connection.open()) {
            return handle.select("SELECT `name` FROM `<TABLE>`  WHERE `uuid` = ?")
                    .define("TABLE", "user")
                    .bind(0, uuid)
                    .mapTo(String.class)
                    .stream()
                    .findFirst()
                    .isPresent();
        }
    }
}
