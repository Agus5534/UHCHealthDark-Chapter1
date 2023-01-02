package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "no_nametag",
        name = "&bNo Nametag",
        material = Material.NAME_TAG,
        lore = {"&7- Las nametags de los jugadores no serán visibles."}
)
public class NoNameTagMode extends Modality {
    @Inject
    private TeamManager teamManager;

    public NoNameTagMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        teamManager.getUhcTeams()
                .values()
                .forEach(uhcTeam -> uhcTeam.addFlag(TeamFlags.HIDE_NICKNAMES));
    }


    @EventHandler
    public void onLaterScatter(PlayerLaterScatterEvent event) {
        var uhcTeam = teamManager.getPlayerTeam(event.getPlayer().getUniqueId());

        if(uhcTeam != null) {
            uhcTeam.addFlag(TeamFlags.HIDE_NICKNAMES);
        }
    }

    @EventHandler
    public void onTeamCreate(TeamCreateEvent event) {
        event.getUhcTeam().addFlag(TeamFlags.HIDE_NICKNAMES);
    }

    @Override
    public void desactiveMode() {
        super.desactiveMode();
        teamManager.getUhcTeams().values().forEach(team -> team.removeFlag(TeamFlags.HIDE_NICKNAMES));
    }
}
