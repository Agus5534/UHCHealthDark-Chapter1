package io.github.wickeddroidmx.plugin.module;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.cache.SimpleListCache;
import io.github.wickeddroidmx.plugin.cache.SimpleMapCache;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
import io.github.wickeddroidmx.plugin.utils.world.WorldGenerator;
import me.yushust.inject.AbstractModule;
import me.yushust.inject.key.TypeReference;
import org.bukkit.plugin.Plugin;

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

        bind(WorldGenerator.class).toInstance(new WorldGenerator(plugin));
        bind(Main.class).toInstance(plugin);
        bind(Plugin.class).to(Main.class);
    }
}
