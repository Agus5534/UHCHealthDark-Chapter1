package io.github.wickeddroidmx.plugin;

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
import io.github.wickeddroidmx.plugin.utils.files.Configuration;
import io.github.wickeddroidmx.plugin.utils.region.Region;
import io.github.wickeddroidmx.plugin.utils.world.WorldGenerator;
import me.yushust.inject.InjectAll;
import me.yushust.inject.InjectIgnore;
import me.yushust.inject.Injector;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.RenderType;
import org.bukkit.scoreboard.Team;

import javax.inject.Named;
import java.io.IOException;
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
    private Configuration configuration;
    @InjectIgnore
    private Region ARENA;

    @InjectIgnore
    private Region ARENA_SPAWN;

    @InjectIgnore
    private Region ARENA_FALL;
    private Rank rank;

    @Override
    public void onEnable() {
        Injector.create(new MainModule(this))
                        .injectMembers(this);

        loader.load();

        ARENA = new Region(
                new Location(Bukkit.getWorlds().get(0), 301, 62, 306),
                new Location(Bukkit.getWorlds().get(0), 180, 7, 182)
        );

        ARENA_SPAWN = new Region(
                new Location(Bukkit.getWorlds().get(0), 215, 53, 237),
                new Location(Bukkit.getWorlds().get(0), 237, 65, 216)
        );

        ARENA_FALL = new Region(
                new Location(Bukkit.getWorlds().get(0), 222, 57, 223),
                new Location(Bukkit.getWorlds().get(0), 229, 53, 230)
        );

        configuration = new Configuration(this, "config");

        Bukkit.getScoreboardManager().getMainScoreboard().getTeams()
                .forEach(Team::unregister);

        worldGenerator.getUhcWorld().createWorld();

        getServer().getScheduler().runTaskTimer(this, () -> cache.values().forEach(uhcScoreboard -> uhcScoreboard.update(this, modeManager, gameManager, playerManager, teamManager)), 0L, 20L);

        createScoreboard();

        gameManager.setUhcId(new UhcIdLoader().getID());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, ()-> pollManager.updatePollStatus(this),2L, 10L);

        Bukkit.getWorlds().get(0).setPVP(true);

        Bukkit.getScheduler().runTaskLater(this, ()-> clearArena(), 300L);

        Bukkit.getScheduler().runTask(this, ()->Bukkit.getWorlds().forEach(w -> Bukkit.getLogger().info(w.getName())));
    }

    @Override
    public void onDisable() {
        try {
            worldGenerator.deleteWorlds();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void clearArena() {
        getARENA().getBlocksTypeOf(Material.OAK_LEAVES).forEach(block -> block.setType(Material.AIR));
        getARENA().getBlocksTypeOf(Material.COBWEB).forEach(block -> block.setType(Material.AIR));
        getARENA().getBlocksTypeOf(Material.LAVA).forEach(block -> block.setType(Material.AIR));
        getARENA().getBlocksTypeOf(Material.OBSIDIAN).forEach(block -> block.setType(Material.AIR));
        getARENA().getBlocksTypeOf(Material.COBBLESTONE).forEach(block -> block.setType(Material.AIR));
        getARENA().getBlocksTypeOf(Material.WATER).forEach(block -> block.setType(Material.AIR));
        getARENA().getBlocksTypeOf(Material.STONE).forEach(block -> block.setType(Material.AIR));
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

    public Region getARENA_SPAWN() {
        return ARENA_SPAWN;
    }

    public Region getARENA_FALL() {
        return ARENA_FALL;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
