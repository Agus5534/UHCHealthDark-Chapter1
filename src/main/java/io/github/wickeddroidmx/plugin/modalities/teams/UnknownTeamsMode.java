package io.github.wickeddroidmx.plugin.modalities.teams;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&6Unknown Teams",
        material = Material.MAP,
        experimental = true,
        modalityType = ModalityType.TEAM,
        key = "unknown_team",
        lore = {
                "&7- Los teams se revelan a X min.",
                "&7- Activa el Friendly Fire entre los equipos.",
                "&7- Agrega /staffgame settings revealtime [minuto]"
        }
)
public class UnknownTeamsMode extends Modality {

    @Inject
    private GameManager gameManager;

    @Inject
    private TeamManager teamManager;

    @Inject
    private Main plugin;

    public UnknownTeamsMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onTeamCreate(TeamCreateEvent event) {
        if(gameManager.getCurrentTime() >= gameManager.getRevealTime()) { return; }

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            event.getUhcTeam().addFlag(TeamFlags.BLOCK_TEAM_CHAT);
            event.getUhcTeam().addFlag(TeamFlags.BLOCK_TEAM_LOCATION);
            event.getUhcTeam().removeFlag(TeamFlags.ANYONE_CAN_MODIFY);
            event.getUhcTeam().addFlag(TeamFlags.BLOCK_COLOR_CHANGE);
            event.getUhcTeam().addFlag(TeamFlags.HIDE_TAB_NICKNAMES);
            event.getUhcTeam().addFlag(TeamFlags.BLOCK_PREFIX_CHANGE);
            event.getUhcTeam().addFlag(TeamFlags.BLOCK_NAME_CHANGE);
            event.getUhcTeam().addFlag(TeamFlags.FRIENDLY_FIRE);
        },1L);
    }

    @EventHandler
    public void onGameTick(GameTickEvent event) {
        if(event.getTime() == gameManager.getRevealTime()-60) {
            Bukkit.broadcast(ChatUtils.formatComponentPrefix("En 1 minuto se revelarán los teams!"));
        }

        if(event.getTime() == gameManager.getRevealTime()) {

            Bukkit.broadcast(ChatUtils.formatComponentPrefix("Se han revelado los teams!"));

            Bukkit.getScheduler().runTaskLater(plugin, ()->{
                teamManager.getUhcTeams().values()
                        .stream()
                        .forEach(uhcTeam -> {
                            uhcTeam.removeFlag(TeamFlags.BLOCK_TEAM_CHAT);
                            uhcTeam.removeFlag(TeamFlags.BLOCK_TEAM_LOCATION);
                            uhcTeam.addFlag(TeamFlags.ANYONE_CAN_MODIFY);
                            uhcTeam.removeFlag(TeamFlags.BLOCK_COLOR_CHANGE);
                            uhcTeam.removeFlag(TeamFlags.HIDE_TAB_NICKNAMES);
                            uhcTeam.removeFlag(TeamFlags.BLOCK_PREFIX_CHANGE);
                            uhcTeam.removeFlag(TeamFlags.BLOCK_NAME_CHANGE);
                            uhcTeam.removeFlag(TeamFlags.FRIENDLY_FIRE);
                        });
            }, 1L);
        }
    }


}
