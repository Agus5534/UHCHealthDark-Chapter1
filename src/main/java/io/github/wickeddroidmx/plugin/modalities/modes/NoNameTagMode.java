package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.scoreboard.Team;

import javax.inject.Inject;

public class NoNameTagMode extends Modality {
    @Inject
    private TeamManager teamManager;

    public NoNameTagMode() {
        super(ModalityType.MODE, "no_nametag", "&8No Nametag", Material.NAME_TAG,
                ChatUtils.format("&7- Las nametags de los jugadores no se veran."));
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        teamManager.getUhcTeams()
                .values()
                .forEach(uhcTeam -> uhcTeam.getTeam().setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.FOR_OTHER_TEAMS));
    }
}
