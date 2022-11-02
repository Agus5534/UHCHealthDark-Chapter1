package io.github.wickeddroidmx.plugin.services;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.team.TeamFlagChangedEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamWinEvent;
import io.github.wickeddroidmx.plugin.listeners.block.BlockBreakListener;
import io.github.wickeddroidmx.plugin.listeners.block.BlockPlaceListener;
import io.github.wickeddroidmx.plugin.listeners.chat.AsyncChatListener;
import io.github.wickeddroidmx.plugin.listeners.chunk.ChunkLoadListener;
import io.github.wickeddroidmx.plugin.listeners.custom.*;
import io.github.wickeddroidmx.plugin.listeners.entities.EntityPickupItemListener;
import io.github.wickeddroidmx.plugin.listeners.players.*;
import io.github.wickeddroidmx.plugin.listeners.portal.PlayerPortalListener;
import io.github.wickeddroidmx.plugin.listeners.team.*;
import io.github.wickeddroidmx.plugin.listeners.entities.EntityDamageByEntityListener;
import io.github.wickeddroidmx.plugin.listeners.entities.EntityDamageListener;
import io.github.wickeddroidmx.plugin.listeners.worldborder.WorldBorderMoveListener;
import io.github.wickeddroidmx.plugin.listeners.worldborder.WorldBorderSetListener;
import me.yushust.inject.InjectAll;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;
import team.unnamed.gui.core.GUIListeners;

@InjectAll
public class ListenerLoader implements Loader {

    private Main plugin;

    //Player Events
    private PlayerJoinListener playerJoinListener;
    private PlayerQuitListener playerQuitListener;
    private PlayerDeathListener playerDeathListener;
    private PlayerScatteredListener playerScatteredListener;
    private FoodLevelChangeListener foodLevelChangeListener;
    private AsyncChatListener asyncChatListener;
    private PlayerInteractAtEntityListener playerInteractAtEntityListener;

    private PlayerPreLoginListener playerPreLoginListener;

    // Block Events
    private BlockBreakListener blockBreakListener;

    private BlockPlaceListener blockPlaceListener;

    //Entity Events
    private EntityDamageByEntityListener entityDamageByEntityListener;
    private EntityDamageListener entityDamageListener;
    private EntityPickupItemListener entityPickupItemListener;

    // Portal Events
    private PlayerPortalListener portalListener;

    //Custom Events
    private GameTickListener gameTickListener;
    private GameStartListener gameStartListener;
    private ActiveModeListener activeModeListener;
    private DesactiveModeListener desactiveModeListener;
    private ChangeGameTimeListener changeGameTimeListener;

    //Team Events
    private TeamCreateListener teamCreateListener;
    private TeamDeleteListener teamDeleteListener;
    private TeamWinListener teamWinListener;
    private PlayerJoinedTeamListener playerJoinedTeamListener;
    private PlayerLeaveTeamListener playerLeaveTeamListener;
    private PlayerPromotedTeamListener playerPromotedTeamListener;
    private TeamFlagChangedListener teamFlagChangedListener;

    // WorldBorder Events
    private WorldBorderSetListener worldBorderSetListener;
    private WorldBorderMoveListener worldBorderMoveListener;

    // LOGS events

    private PlayerLogsListener playerLogsListener;

    // ????

    private WaitingStatusListeners statusListeners;



    @Override
    public void load() {
        registerListeners(
                playerJoinListener,
                playerQuitListener,
                foodLevelChangeListener,
                asyncChatListener,
                entityDamageListener,
                entityDamageByEntityListener,
                playerDeathListener,
                blockBreakListener,
                gameTickListener,
                activeModeListener,
                desactiveModeListener,
                worldBorderSetListener,
                teamCreateListener,
                teamDeleteListener,
                playerJoinedTeamListener,
                playerLeaveTeamListener,
                playerScatteredListener,
                playerPromotedTeamListener,
                gameStartListener,
                worldBorderMoveListener,
                portalListener,
                playerPreLoginListener,
                teamWinListener,
                changeGameTimeListener,
                blockPlaceListener,
                playerLogsListener,
                playerInteractAtEntityListener,
                entityPickupItemListener,
                teamFlagChangedListener,
                statusListeners
        );
    }

    private void registerListeners(Listener... listeners) {
        Bukkit.getPluginManager().registerEvents(new GUIListeners(), plugin);

        for (Listener listener : listeners) {
            Bukkit.getPluginManager().registerEvents(listener, plugin);
        }
    }
}
