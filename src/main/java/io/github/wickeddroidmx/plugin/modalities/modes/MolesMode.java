package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
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
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

@GameModality(
        name = "&4Moles",
        key = "moles",
        material = Material.IRON_SWORD,
        modalityType = ModalityType.MODE,
        lore = {
                "&7- Equipos de 3 jugadores.",
                "&7- Un impostor por equipo.",
                "&7- La partida terminará siendo To2.",
                "&7- &bNo tiene función, solo visual."
        }
)
public class MolesMode extends Modality {

    @Inject
    private PlayerManager playerManager;

    @Inject
    private TeamManager teamManager;


    List<Player> molesList;
    HashMap<Player, Player> molesTeams;
    public MolesMode() throws IllegalClassFormatException {
        super();

       // teamManager.setTeamSize(3);
        molesList = new ArrayList<>();

        molesTeams = new HashMap<>();
    }

   /* @EventHandler
    public void onGameStart(GameStartEvent event) {
        for(var uhcTeam : teamManager.getUhcTeams().values()) {
            if(uhcTeam.getMole() == null) {
               List<Player> teamPlayers = new ArrayList<>();
               uhcTeam.getTeamPlayers().forEach(p -> teamPlayers.add(Bukkit.getPlayer(p)));

               Player mole = teamPlayers.get(new Random().nextInt(teamPlayers.size()));

               if(uhcTeam.getOwner() == mole && uhcTeam.getTeamPlayers().size() >= 2) {
                   teamPlayers.remove(mole);

                   uhcTeam.setOwner(teamPlayers.stream().findFirst().orElse(null));
               }

               uhcTeam.setMole(mole);
            }
        }
    }

    @EventHandler
    public void gameTick(GameTickEvent event) {
        if(event.getTime() % (60 * 60) == 0) {

            for(var uhcTeam : teamManager.getUhcTeams().values()) {
                uhcTeam.getTeam().setAllowFriendlyFire(true);
                molesList.add(uhcTeam.getMole());
            }

            for(int i = 0; i < molesList.size(); i++) {
                Player p1 = null;
                Player p2;
                if(i%2 == 0) {
                    p1 = molesList.get(i);
                }

                if(molesList.size() < i+1) {
                    molesTeams.put(p1, null);
                    return;
                }

                p2 = molesList.get(i+1);

                molesTeams.put(p1, p2);
            }

            for(Player p2 : molesTeams.values()) {
                for(Player p : molesTeams.keySet()) {
                    if(p2 == molesTeams.get(p)) {
                        p2.sendMessage(ChatUtils.TEAM + ChatUtils.format("&7Eres el &4MOLE &7junto a &4"+molesTeams.get(p).getName()));
                    }
                }
            }

            for(Player p : molesTeams.keySet()) {
                if(molesTeams.get(p) == null) {
                    p.sendMessage(ChatUtils.TEAM + ChatUtils.format("&7Eres el &4MOLE &7pero no tienes team"));

                    if(!playerManager.getPlayer(p.getUniqueId()).isAlive()) {
                        return;
                    }

                    teamManager.getPlayerTeam(p.getUniqueId()).leavePlayer(p);

                    teamManager.createTeam(p);

                    molesTeams.remove(p);


                    return;
                }

                Player team = molesTeams.get(p);

                p.sendMessage(ChatUtils.TEAM + ChatUtils.format("&7Eres el &4MOLE &7junto a &4"+team.getName()));
            }

        }

        if(event.getTime() % (60 * 90) == 0) {
            for(Player p : molesTeams.keySet()) {
                if(teamManager.getPlayerTeam(p.getUniqueId()).getMole() == p) {
                    teamManager.getPlayerTeam(p.getUniqueId()).leavePlayer(p);

                    teamManager.createTeam(p);

                    //TERMINAR ESTO
                }
            }
        }
    }

    @EventHandler
    public void onTeamDelete(TeamDeleteEvent event) {
        var uhcTeam = event.getUhcTeam();
        if(molesTeams.containsKey(uhcTeam.getMole())) {
            event.getUhcTeam().leavePlayer(uhcTeam.getMole());

            var moleTeam = molesTeams.get(uhcTeam.getMole());

            molesTeams.remove(uhcTeam.getMole());

            teamManager.createTeam(uhcTeam.getMole());

            teamManager.getPlayerTeam(moleTeam.getUniqueId()).leavePlayer(moleTeam);

            teamManager.getPlayerTeam(uhcTeam.getMole().getUniqueId()).addPlayer(moleTeam);

            teamManager.getPlayerTeam(uhcTeam.getMole().getUniqueId()).decrementPlayersAlive();

        }

        if(molesTeams.values().contains(uhcTeam.getMole())) {
            Player moleTeam = null;
            for(var m : molesTeams.keySet()) {
                if(molesTeams.get(m) == uhcTeam.getMole()) {
                    moleTeam = m;
                }
            }

            teamManager.createTeam(uhcTeam.getMole());

            teamManager.getPlayerTeam(moleTeam.getUniqueId()).leavePlayer(moleTeam);

            teamManager.getPlayerTeam(uhcTeam.getMole().getUniqueId()).addPlayer(moleTeam);

            teamManager.getPlayerTeam(uhcTeam.getMole().getUniqueId()).decrementPlayersAlive();

            molesTeams.remove(moleTeam);
        }
    }*/
}
