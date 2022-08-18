package io.github.wickeddroidmx.plugin.connection;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.bukkit.Bukkit;
import org.jdbi.v3.core.Jdbi;

public class SQLClient {

    private final Jdbi jdbi;

    public SQLClient(HikariDataSource dataSource) {
        jdbi = Jdbi.create(dataSource);
    }

    public Jdbi getConnection() {
        return jdbi;
    }

    public static class Builder {

        private final String JDBC_FORMAT = "jdbc:mysql://%s:%s/%s";
        private final HikariConfig config = new HikariConfig();
        private String host;
        private String database;
        private int port;
        private int maximumPoolSize = 0;
        private String table;

        public Builder setDatabase(String database) {
            this.database = database;
            return this;
        }

        public Builder setHost(String host) {
            this.host = host;
            return this;
        }

        public Builder setPort(int port) {
            this.port = port;
            return this;
        }

        public Builder setTable(String table) {
            this.table = table;
            return this;
        }

        public Builder setUsername(String username) {
            config.setUsername(username);
            return this;
        }

        public Builder setPassword(String password) {
            config.setPassword(password);
            return this;
        }

        public Builder setMaximumPoolSize(int maximumPoolSize) {
            this.maximumPoolSize = maximumPoolSize;
            return this;
        }

        public String getTable() {
            return table;
        }

        public SQLClient build() {
            config.setJdbcUrl(String.format(
                    JDBC_FORMAT,
                    host, port, database
            ));

            config.setMaximumPoolSize(maximumPoolSize);

            return new SQLClient(new HikariDataSource(config));
        }
    }
}
