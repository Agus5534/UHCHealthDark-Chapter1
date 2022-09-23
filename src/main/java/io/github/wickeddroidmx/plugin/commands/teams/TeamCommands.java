package io.github.wickeddroidmx.plugin.commands.teams;

import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.PlayerPromotedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.menu.UhcTeamMenu;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

@Command(
        names="team"
)
public class TeamCommands implements CommandClass {

    @Inject
    private TeamManager teamManager;

    @Inject
    private GameManager gameManager;

    @Inject
    private UhcTeamMenu uhcTeamMenu;

    @Command(
            names = "invite"
    )
    public void teamInviteCommand(@Sender Player sender, Player target) {
        var targetTeam = teamManager.getPlayerTeam(target.getUniqueId());
        var senderTeam = teamManager.getPlayerTeam(sender.getUniqueId());

        if(senderTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes un equipo.");
            return;
        }

        if (sender == target) {
            sender.sendMessage(ChatUtils.PREFIX + "No te puedes invitar a ti mismo.");
            return;
        }

        if (gameManager.getGameState() != GameState.WAITING) {
            sender.sendMessage(ChatUtils.PREFIX + "No puedes hacer esto una vez iniciada la partida.");
            return;
        }

        if (teamManager.getTeamInvite(sender, target) != null) {
            sender.sendMessage(ChatUtils.PREFIX + "Ya has invitado a ese jugador.");
            return;
        }

        if (targetTeam != null) {
            sender.sendMessage(ChatUtils.PREFIX + "El usuario ya tiene equipo.");
            return;
        }

        teamManager.createInvite(sender, target);

        sender.sendMessage(ChatUtils.TEAM + ChatUtils.format(String.format("Se ha invitado correctamente a &6%s", target.getName())));
        target.sendMessage(ChatUtils.TEAM + ChatUtils.format(String.format("Te ha invitado %s a su equipo.", sender.getName())));
    }

    @Command(
            names = "accept"
    )
    public void acceptCommand(@Sender Player sender, Player target) {
        var uhcTeam = teamManager.getPlayerTeam(target.getUniqueId());
        var targetTeam = teamManager.getTeam(sender.getUniqueId());
        var teamInvite = teamManager.getTeamInvite(target, sender);

        if (targetTeam != null) {
            sender.sendMessage(ChatUtils.PREFIX + "Ya te encuentras en un equipo, no puedes aceptar esta invitación.");
            return;
        }

        if (gameManager.getGameState() != GameState.WAITING) {
            sender.sendMessage(ChatUtils.PREFIX + "No puedes hacer esto una vez iniciada la partida.");
            return;
        }

        if (teamInvite == null) {
            sender.sendMessage(ChatUtils.PREFIX + "Este equipo no te ha invitado.");
            return;
        }

        if (uhcTeam == null) {
            teamManager.createTeam(target);

            uhcTeam = teamManager.getPlayerTeam(target.getUniqueId());
            Bukkit.getPluginManager().callEvent(new TeamCreateEvent(uhcTeam, target));
        }

        if (uhcTeam.getSize() == teamManager.getTeamSize()) {
            sender.sendMessage(ChatUtils.PREFIX + "No puedes unirte a este equipo, se encuentra lleno.");
            return;
        }

        Bukkit.getPluginManager().callEvent(new PlayerJoinedTeamEvent(uhcTeam, sender));
    }

    @Command(
            names = "name"
    )
    public void nameCommand(@Sender Player sender, @Text String text) {
        var uhcTeam = teamManager.getPlayerTeam(sender.getUniqueId());

        if (uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes un equipo.");
            return;
        }

        if (text.length() > 16) {
            sender.sendMessage(ChatUtils.PREFIX + "Has pasado el limite de caracteres.");
            return;
        }

        sender.sendMessage(ChatUtils.TEAM + ChatUtils.format(String.format("El nombre del equipo ha cambiado a &6%s", text)));
        uhcTeam.setPrefix(text);
    }

    @Command(
            names = "prefix"
    )
    public void prefixCommand(@Sender Player sender, @Text String text) {
        var uhcTeam = teamManager.getPlayerTeam(sender.getUniqueId());

        List<String> teamPrefixes = new ArrayList<>();

        for(var uhcT : teamManager.getUhcTeams().values()) {
            if(uhcT.getTeam().getPrefix() == null) { continue; }
            teamPrefixes.add(ChatColor.stripColor(uhcT.getTeam().getPrefix()));
        }

        if (uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes un equipo.");
            return;
        }

        if(uhcTeam.getTeam().getPrefix() != null) {
            if(teamPrefixes.contains(ChatColor.stripColor(text))) {
                sender.sendMessage(ChatUtils.TEAM + "Ya hay un team con ese prefix.");
                return;
            }
        }

        if (text.length() > 16) {
            sender.sendMessage(ChatUtils.PREFIX + "Has pasado el limite de caracteres.");
            return;
        }

        sender.sendMessage(ChatUtils.TEAM + ChatUtils.format(String.format("El prefix del equipo ha cambiado a &6%s", text)));
        uhcTeam.setPrefix(text);
    }

    @Command(
            names = "promote"
    )
    public void promoteCommand(@Sender Player sender, Player target) {
        var team = teamManager.getPlayerTeam(sender.getUniqueId());

        if (team == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes un equipo.");
            return;
        }

        if (gameManager.getGameState() != GameState.WAITING) {
            sender.sendMessage(ChatUtils.PREFIX + "No puedes hacer esto una vez iniciada la partida.");
            return;
        }

        if (sender == target) {
            sender.sendMessage(ChatUtils.PREFIX + "No puedes promotearte a ti mismo.");
            return;
        }

        Bukkit.getPluginManager().callEvent(new PlayerPromotedTeamEvent(team, target));
    }

    @Command(
            names = "list"
    )
    public void listCommand(@Sender Player sender) {
        if (teamManager.getUhcTeams().size() == 0) {
            sender.sendMessage(ChatUtils.PREFIX + "No se ha creado ningún equipo.");
            return;
        }

        sender.openInventory(uhcTeamMenu.getTeamList());
    }
}
