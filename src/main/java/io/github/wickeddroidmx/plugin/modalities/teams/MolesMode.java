package io.github.wickeddroidmx.plugin.modalities.teams;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamDeleteEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        name = "&4Moles",
        key = "moles",
        material = Material.BOW,
        modalityType = ModalityType.TEAM,
        lore = {
                "&7- Equipos de 3 jugadores.",
                "&7- Un impostor por equipo.",
                "&7- La partida terminará siendo To2.",
                "&7- &bDesactiva las detección de victoria."
        },
        experimental = true
)
public class MolesMode extends Modality {

    @Inject
    private PlayerManager playerManager;

    @Inject
    private TeamManager teamManager;
    @Inject
    private GameManager gameManager;
    List<OfflinePlayer> molesList;
    HashMap<Player, Player> molesTeams;
    private int revealTime = 0;
    public MolesMode() throws IllegalClassFormatException {
        super();

       // teamManager.setTeamSize(3);
        molesList = new ArrayList<>();

        molesTeams = new HashMap<>();
    }

    @Override
    public void activeMode() {
        super.activeMode();
        teamManager.setTeamSize(3);
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        int maxTime = gameManager.getTimeForMeetup() - 600;

        revealTime = ThreadLocalRandom.current().nextInt(300, maxTime);
    }

    @EventHandler
    public void onGameTick(GameTickEvent event) {
        if(event.getTime() == revealTime) {
            setMoles();
            //REVEAL
        }
    }

    public void setMoles() {
        for(var t : teamManager.getUhcTeams().values()) {
            if(!t.isAlive()) { continue; }
            if(t.getPlayersAlive() <= 0) { continue; }

            List<OfflinePlayer> teamPlayers = new ArrayList<>();

            for(var uuid : t.getTeamPlayers()) {
                teamPlayers.add(Bukkit.getOfflinePlayer(uuid));
            }

            Collections.shuffle(teamPlayers);
            molesList.add(teamPlayers.stream().findFirst().get());

            for(var p : molesList) {
                teamManager.getTeam(p.getUniqueId()).setMole(p.getPlayer());
            }

        }
    }
}
