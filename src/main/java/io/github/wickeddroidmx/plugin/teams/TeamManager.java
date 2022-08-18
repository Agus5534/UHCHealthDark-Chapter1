package io.github.wickeddroidmx.plugin.teams;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.*;
import java.util.stream.Collectors;

@Singleton
public class TeamManager  {

    @Inject
    private Main plugin;

    private final HashMap<UUID, UhcTeam> uhcTeams = new HashMap<>();
    private final List<TeamInvite> teamInvites = new ArrayList<>();

    private int teamSize,
                currentTeams;

    public TeamManager() {
        this.teamSize = 2;
        this.currentTeams = 0;
    }

    public UhcTeam createTeam(Player owner) {
        ++this.currentTeams;

        return uhcTeams.put(owner.getUniqueId(), new UhcTeam(owner, currentTeams));
    }

    public UhcTeam getTeam(UUID uuid) {
        return uhcTeams.get(uuid);
    }

    public UhcTeam removeTeam(Player owner) {
        --this.currentTeams;

        var uhcTeam = uhcTeams.remove(owner.getUniqueId());

        uhcTeam.getTeam().unregister();

        return uhcTeam;
    }

    public void sendMessage(UUID uuid, String text) {
        var team = getPlayerTeam(uuid);

        team.getTeamPlayers()
                .stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .filter(Player::isOnline)
                .forEach(player -> player.sendMessage(ChatUtils.TEAM + text));
    }

    public void createInvite(Player owner, Player player) {
        var teamInvite = new TeamInvite(owner, player, getTeam(owner.getUniqueId()));

        teamInvites.add(teamInvite);

        timeInvite(owner, player);
    }

    public TeamInvite getTeamInvite(Player owner, Player player) {
        return teamInvites
                .stream()
                .filter(teamInvite -> teamInvite.getOwner() == owner)
                .filter(teamInvite -> teamInvite.getReceived() == player)
                .findFirst()
                .orElse(null);
    }

    private void timeInvite(Player owner, Player player) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            var invite = getTeamInvite(owner, player);

            if (invite != null) {
                teamInvites.remove(invite);

                if (owner.isOnline())
                    owner.sendMessage(ChatUtils.TEAM + "Se ha eliminado la invitación de " + player.getName());

                if (player.isOnline())
                    player.sendMessage(ChatUtils.TEAM + "Se ha eliminado la invitación de " + owner.getName());
            }
        }, 200L);
    }

    public void randomizeTeam() {
        if (teamSize > 1) {
            var playersWithoutTeam = Bukkit.getOnlinePlayers()
                    .stream()
                    .filter(player -> getPlayerTeam(player.getUniqueId()) == null)
                    .collect(Collectors.toCollection(ArrayList::new));

            Collections.shuffle(playersWithoutTeam);

            var playersWithoutTeamSize = playersWithoutTeam.size();
            var teamsNeeded = (int) Math.ceil(playersWithoutTeamSize / teamSize) + 1;

            try {
                for (int i = 0;i<teamsNeeded;i++) {
                    var leader = playersWithoutTeam.remove(0);
                    createTeam(leader);

                    var team = getPlayerTeam(leader.getUniqueId());

                    if (team != null) {
                        Bukkit.getPluginManager().callEvent(new TeamCreateEvent(team, leader));

                        for (int j = 0;j<teamSize - 1;j++) {
                            var member = playersWithoutTeam.remove(0);

                            Bukkit.getPluginManager().callEvent(new PlayerJoinedTeamEvent(team, member));
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setTeamSize(int teamSize) {
        this.teamSize = teamSize;
    }

    public void setCurrentTeams(int currentTeams) {
        this.currentTeams = currentTeams;
    }

    public int getTeamSize() {
        return teamSize;
    }

    public String getFormatTeamSize() {
        if (teamSize > 1)
            return "To" + teamSize;

        return "FFA";
    }

    public int getCurrentTeams() {
        return currentTeams;
    }

    public List<TeamInvite> getTeamInvites() {
        return teamInvites;
    }

    public UhcTeam getPlayerTeam(UUID uuid) {
        return uhcTeams.values()
                .stream()
                .filter(uhcTeam -> uhcTeam.existsPlayer(uuid))
                .findFirst()
                .orElse(null);
    }

    public HashMap<UUID, UhcTeam> getUhcTeams() {
        return uhcTeams;
    }
}
