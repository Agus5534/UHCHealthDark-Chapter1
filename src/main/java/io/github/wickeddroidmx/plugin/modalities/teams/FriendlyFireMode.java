package io.github.wickeddroidmx.plugin.modalities.teams;

import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&4Friendly Fire",
        material = Material.REDSTONE,
        modalityType = ModalityType.TEAM,
        key = "friendly_fire"
)
public class FriendlyFireMode extends Modality {

    @Inject
    private TeamManager teamManager;

    public FriendlyFireMode() throws IllegalClassFormatException {
        super(ChatUtils.format("&7- El FriendlyFire se activa"));
    }

    @EventHandler
    public void onTeamCreate(TeamCreateEvent event) {
        event.getUhcTeam().setFriendlyFire(true);
    }

    @Override
    public void activeMode() {
        super.activeMode();

        for(var team : teamManager.getUhcTeams().values()) {
            team.setFriendlyFire(true);
        }
    }

    @Override
    public void desactiveMode() {
        super.desactiveMode();

        for(var team : teamManager.getUhcTeams().values()) {
            team.setFriendlyFire(false);
        }
    }
}
