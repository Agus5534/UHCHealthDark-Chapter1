package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.PlayerLeaveTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamDeleteEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.menu.UhcTeamMenu;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.stream.Collectors;

@Command(
        names="staffteam",
        permission = "healthdark.staffteam"
)
public class StaffTeamCommands implements CommandClass {

    @Inject
    private TeamManager teamManager;

    @Inject
    private UhcTeamMenu uhcTeamMenu;

    @Inject
    private PlayerManager playerManager;

    @Command(
            names = "create"
    )
    public void createCommand(@Sender Player sender, Player target) {
        var teamPlayer = teamManager.getTeam(target.getUniqueId());

        if (teamPlayer != null) {
            sender.sendMessage(ChatUtils.PREFIX + "El usuario ya tiene un equipo.");
            return;
        }

        teamManager.createTeam(target);

        Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getTeam(target.getUniqueId()), target));
    }

    @Command(
            names = "join"
    )
    public void joinCommand(@Sender Player sender, Player target) {
        var teamPlayer = teamManager.getTeam(target.getUniqueId());

        if (teamPlayer != null) {
            var findRandomPlayer = teamPlayer.getTeamPlayers().stream().findAny().orElse(null);

            teamPlayer.leavePlayer(target);

            if (teamPlayer.getSize() > 1) {
                if (findRandomPlayer == null) {
                    return;
                }

                var player = Bukkit.getPlayer(findRandomPlayer);

                if (player != null && player.isOnline()) {
                    teamPlayer.setOwner(player);
                }
            } else {
                Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(teamPlayer));
            }
        }


        sender.openInventory(
            uhcTeamMenu.getJoinInventory(sender, target)
        );
    }

    @Command(
            names = "leave"
    )
    public void leaveCommand(@Sender Player sender, Player target) {
        var uhcTeam = teamManager.getPlayerTeam(target.getUniqueId());

        if (uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "El usuario no tiene equipo.");
            return;
        }

        if (uhcTeam.getOwner() == target) {
            sender.sendMessage(ChatUtils.PREFIX + "El usuario es el dueño del equipo.");
            return;
        }

        Bukkit.getPluginManager().callEvent(new PlayerLeaveTeamEvent(uhcTeam, target));
    }

    @Command(
            names = "delete"
    )
    public void teamDeleteCommand(@Sender Player sender, Player target) {
        var uhcTeam = teamManager.getPlayerTeam(target.getUniqueId());

        if (uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
            return;
        }

        Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(uhcTeam));
    }

    @Command(
            names = "deletewithname"
    )
    public void deleteWithNameCommand(@Sender Player sender, @Text String text) {
        var uhcTeam = teamManager.getUhcTeams()
                .values()
                .stream()
                .filter(team -> team.getName().contains(text))
                .findFirst()
                .orElse(null);

        if (uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
            return;
        }

        Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(uhcTeam));
    }

    @Command(
            names = "randomize"
    )
    public void randomizeCommand(@Sender Player sender) {
       teamManager.randomizeTeam();
    }

    @Command(
            names = "ffa"
    )
    public void ffaCommand(@Sender Player sender) {
        for (var player : Bukkit.getOnlinePlayers()) {
            if (teamManager.getPlayerTeam(player.getUniqueId()) != null)
                Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(teamManager.getPlayerTeam(player.getUniqueId())));

            if(playerManager.getPlayer(player.getUniqueId()) != null) {
                if(playerManager.getPlayer(player.getUniqueId()).isSpect()) { continue; }
            }

            teamManager.createTeam(player);

            Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getPlayerTeam(player.getUniqueId()), player));
        }
    }

    @Command(
            names = "size"
    )
    public void sizeCommand(@Sender Player sender, int size) {
        teamManager.setTeamSize(size);

        sender.sendMessage(ChatUtils.PREFIX + "El tamaño de equipos es: " + teamManager.getFormatTeamSize());
    }

    @Command(
            names = "doubledates"
    )
    public void doubleDatesCommand(@Sender Player sender) {
        var uhcTeams = new ArrayList<>(teamManager.getUhcTeams().values());

        Collections.shuffle(uhcTeams);

        while (teamManager.getCurrentTeams() > 1) {
            var firstTeam = uhcTeams.get(0);

            Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(firstTeam));
            uhcTeams.remove(firstTeam);

            var secondTeam = uhcTeams.get(0);
            uhcTeams.remove(secondTeam);

            matchTeams(firstTeam, secondTeam);
        }


        sender.sendMessage(ChatUtils.PREFIX + "Se ha hecho el double dates correctamente");
    }



    private void matchTeams(UhcTeam firstTeam, UhcTeam secondTeam) {
        firstTeam.getTeamPlayers()
                .stream()
                .map(Bukkit::getPlayer)
                .filter(Objects::nonNull)
                .filter(Player::isOnline)
                .forEach(player -> Bukkit.getPluginManager().callEvent(new PlayerJoinedTeamEvent(secondTeam, player)));
    }
}
