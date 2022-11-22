package io.github.wickeddroidmx.plugin;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.module.MainModule;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.poll.PollManager;
import io.github.wickeddroidmx.plugin.scoreboard.UHCScoreboard;
import io.github.wickeddroidmx.plugin.services.Loader;
import io.github.wickeddroidmx.plugin.services.UhcIdLoader;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.chat.Rank;
import io.github.wickeddroidmx.plugin.utils.region.Region;
import io.github.wickeddroidmx.plugin.utils.world.WorldGenerator;
import me.yushust.inject.InjectAll;
import me.yushust.inject.InjectIgnore;
import me.yushust.inject.Injector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Team;

import javax.inject.Named;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

@InjectAll
public class Main extends JavaPlugin {

    @Named("main-loader")
    private Loader loader;

    @Named("scoreboard-cache")
    private MapCache<UUID, UHCScoreboard> cache;

    private PlayerManager playerManager;
    private TeamManager teamManager;
    private GameManager gameManager;
    private ModeManager modeManager;
    private PollManager pollManager;
    private WorldGenerator worldGenerator;
    private ExperimentManager experimentManager;
    @InjectIgnore
    private Region ARENA;
    private Rank rank;

    @Override
    public void onEnable() {
        Injector.create(new MainModule(this))
                        .injectMembers(this);

        loader.load();

        ARENA = new Region(
                new Location(Bukkit.getWorlds().get(0), 188, 55, 179),
                new Location(Bukkit.getWorlds().get(0), 302, 12, 308)
        );

        Bukkit.getScoreboardManager().getMainScoreboard().getTeams()
                .forEach(Team::unregister);

        worldGenerator.getUhcWorld().createWorld();

        getServer().getScheduler().runTaskTimer(this, () -> cache.values().forEach(uhcScoreboard -> uhcScoreboard.update(modeManager, gameManager, playerManager, teamManager)), 0L, 20L);

        createScoreboard();

        gameManager.setUhcId(new UhcIdLoader().getID());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ()-> pollManager.updatePollStatus(this),2L, 10L);

        Bukkit.getWorlds().get(0).setPVP(true);
    }

    @Override
    public void onDisable() {
        try {
            worldGenerator.deleteWorlds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    private void createScoreboard() {
        var scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

        var spectatorTeam = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam("spectators");

        spectatorTeam.prefix(Component.text(ChatUtils.format("&7[Espectadores] ")));
        spectatorTeam.color(NamedTextColor.GRAY);

        gameManager.setSpectatorTeam(spectatorTeam);

        if (scoreboard.getObjective("health") == null) {
            var objective = scoreboard.registerNewObjective("health", "health");

            objective.setRenderType(RenderType.HEARTS);
            objective.setDisplaySlot(DisplaySlot.PLAYER_LIST);
        }


    }

    public WorldGenerator getWorldGenerator() {
        return worldGenerator;
    }

    public Region getARENA() {
        return ARENA;
    }
}
