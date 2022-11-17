package io.github.wickeddroidmx.plugin.commands.teams;

import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.listeners.custom.WaitingStatusListeners;
import io.github.wickeddroidmx.plugin.menu.UhcTeamMenu;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.Text;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Inject
    private PlayerManager playerManager;

    @Inject
    private ModeManager modeManager;

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

        if(senderTeam.getSize() >= teamManager.getTeamSize()) {
            sender.sendMessage(ChatUtils.PREFIX + "El equipo ya ha alcanzado el máximo de jugadores posibles");
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
            names = "canmembersmodifyteam"
    )
    public void canMembersModifyCommand(@Sender Player sender, @Named("canMembersModifyTeam") boolean b) {
        var uhcTeam = teamManager.getPlayerTeam(sender.getUniqueId());

        if (uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes un equipo.");
            return;
        }

        if(!uhcTeam.getOwner().equals(sender)) {
            sender.sendMessage(ChatUtils.PREFIX + "No eres el owner del team.");
            return;
        }

        if(uhcTeam.containsFlag(TeamFlags.ANYONE_CAN_MODIFY) == b) {
            sender.sendMessage(ChatUtils.PREFIX + "El team ya tiene esa flag en ese valor.");
            return;
        }

        if(b) { uhcTeam.addFlag(TeamFlags.ANYONE_CAN_MODIFY); } else { uhcTeam.removeFlag(TeamFlags.ANYONE_CAN_MODIFY); }

        uhcTeam.sendMessage(
                String.format("El permiso de los miembros a modificar el team se ha %s",
                        (b ? "&aactivado" : "&cdesactivado"))
        );
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
        var teamOwner = playerManager.getPlayer(uhcTeam.getOwner().getUniqueId());

        if (uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes un equipo.");
            return;
        }

        if(uhcTeam.containsFlag(TeamFlags.BLOCK_NAME_CHANGE)) {
            sender.sendMessage(ChatUtils.TEAM + "El team tiene desactivada la característica de cambiar el nombre.");
            return;
        }

        if (text.length() > 16) {
            sender.sendMessage(ChatUtils.PREFIX + "Has pasado el limite de caracteres.");
            return;
        }

        if(teamOwner != null) {
            if(teamOwner.isAlive()) {
                if(!uhcTeam.containsFlag(TeamFlags.ANYONE_CAN_MODIFY) && !teamOwner.equals(sender)) {
                    sender.sendMessage(ChatUtils.PREFIX + "No eres el owner del team.");
                    return;
                }
            }
        }

        uhcTeam.sendMessage(ChatUtils.format(String.format("El nombre del equipo ha cambiado a &6%s", text)));

        uhcTeam.setName(text);
    }

    @Command(names = "color")
    public void colorCommand(@Sender Player sender, @Named("color") ChatColor color) {
        var uhcTeam = teamManager.getPlayerTeam(sender.getUniqueId());

        if (uhcTeam == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes un equipo.");
            return;
        }

        if(!WaitingStatusListeners.donatorsList.contains(sender)) {
            sender.sendMessage(ChatUtils.PREFIX + "No tienes permiso de usar este comando.");
            return;
        }

        if(uhcTeam.containsFlag(TeamFlags.BLOCK_COLOR_CHANGE) || uhcTeam.containsFlag(TeamFlags.HIDE_TAB_NICKNAMES)) {
            sender.sendMessage(ChatUtils.PREFIX + "El team tiene esta característica desactivada.");
            return;
        }

        if(color == ChatColor.MAGIC) {
            sender.sendMessage(ChatUtils.PREFIX + "Este color se encuentra vetado.");
            return;
        }

        uhcTeam.sendMessage(String.format("%s ha cambiado el color del equipo a %s", sender.getName(), color.name()));

        uhcTeam.setColor(color);
        uhcTeam.getTeam().setColor(uhcTeam.getColor());
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

        if(uhcTeam.containsFlag(TeamFlags.BLOCK_PREFIX_CHANGE)) {
            sender.sendMessage(ChatUtils.TEAM + "El team tiene desactivada la característica de cambiar el prefix.");
            return;
        }

        if(uhcTeam.containsFlag(TeamFlags.BLOCK_PREFIX_SPECIAL_CHARACTERS)) {
            if(containsSpecialCharacters(text)) {
                sender.sendMessage(ChatUtils.formatC(ChatUtils.TEAM + "El prefix contiene caracteres prohibidos."));
                return;
            }
        }

        if(uhcTeam.getTeam().getPrefix() != null) {
            if(teamPrefixes.contains(ChatColor.stripColor(text + " | "))) {
                sender.sendMessage(ChatUtils.TEAM + "Ya hay un team con ese prefix.");
                return;
            }
        }

        var teamOwner = playerManager.getPlayer(uhcTeam.getOwner().getUniqueId());

        if(teamOwner != null) {
            if(teamOwner.isAlive()) {
                if(!uhcTeam.containsFlag(TeamFlags.ANYONE_CAN_MODIFY) && !teamOwner.equals(sender)) {
                    sender.sendMessage(ChatUtils.PREFIX + "No eres el owner del team.");
                    return;
                }
            }
        }


        if (text.length() > 16) {
            sender.sendMessage(ChatUtils.PREFIX + "Has pasado el limite de caracteres.");
            return;
        }

        uhcTeam.sendMessage(ChatUtils.format(String.format("El prefix del equipo ha cambiado a &6%s", text)));
        uhcTeam.setPrefix(text);
    }

    /*
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
    }*/

    @Command(
            names = "list"
    )
    public void listCommand(@Sender Player sender) {
        if(modeManager.isActiveMode("unknown_team")) {
            sender.sendMessage(ChatUtils.formatComponentTeam("La lista no se encuentra disponible"));
            return;
        }

        if (teamManager.getUhcTeams().size() == 0) {
            sender.sendMessage(ChatUtils.PREFIX + "No se ha creado ningún equipo.");
            return;
        }

        sender.openInventory(uhcTeamMenu.getTeamList());
    }

    private boolean containsSpecialCharacters(String s) {
        Pattern pattern = Pattern.compile("[^a-zA-Z0-9]");
        Matcher matcher = pattern.matcher(s);

        return matcher.find();
    }
}
