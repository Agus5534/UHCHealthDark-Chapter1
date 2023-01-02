package io.github.wickeddroidmx.plugin.listeners.team;

import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.events.team.TeamWinEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Objects;
import java.util.UUID;

public class TeamWinListener implements Listener {

    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onTeamWin(TeamWinEvent e) {
        var team = e.getUhcTeam();

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);

            player.sendTitle(ChatUtils.format("&6¡Victoria!"), String.format(ChatUtils.format("&7%s"), team.getName()), 20, 60, 20);
        });

        gameManager.setGameState(GameState.FINISHING);
        gameManager.setTeamWin(team);
    }
}
