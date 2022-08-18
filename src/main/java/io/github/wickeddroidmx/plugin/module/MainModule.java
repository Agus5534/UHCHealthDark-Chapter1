package io.github.wickeddroidmx.plugin.module;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.cache.SimpleListCache;
import io.github.wickeddroidmx.plugin.cache.SimpleMapCache;
import io.github.wickeddroidmx.plugin.connection.SQLClient;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
import io.github.wickeddroidmx.plugin.sql.model.User;
import io.github.wickeddroidmx.plugin.utils.files.Configuration;
import me.yushust.inject.AbstractModule;
import me.yushust.inject.key.TypeReference;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.UUID;

public class MainModule extends AbstractModule {

    private final Main plugin;

    public MainModule(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public void configure() {
        install(new LoaderModule());

        bind(new TypeReference<ListCache<UUID>>() {})
                .named("ironman-cache")
                .toInstance(new SimpleListCache<>());

        bind(new TypeReference<MapCache<UUID, UHCScoreboard>>() {})
                .named("scoreboard-cache")
                .toInstance(new SimpleMapCache<>());

        bind(new TypeReference<MapCache<UUID, User>>() {})
                .named("user-cache")
                        .toInstance(new SimpleMapCache<>());

        bind(Main.class).toInstance(plugin);
        bind(Plugin.class).to(Main.class);

        bind(SQLClient.class)
                .named("sql-user")
                        .toInstance(new SQLClient.Builder()
                                .setHost("a31r0e8m3dzj.us-east-3.psdb.cloud")
                                .setPort(3306)
                                .setDatabase("healthdark")
                                .setUsername("xz49mrhhq8r1")
                                .setPassword("pscale_pw_oqm1_Lc3aP_rlOqig_8UhuxXQH4Zux96bOxl1N2pP4Y")
                                .setMaximumPoolSize(6)
                                .setTable("user")
                                .build());

        bind(Configuration.class)
                .named("file-config")
                .toInstance(new Configuration(plugin, "config"));
    }
}
