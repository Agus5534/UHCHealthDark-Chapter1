package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.PlayerLeaveTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamDeleteEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.menu.UhcTeamMenu;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.*;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Command(
        names="staffteam",
        permission = "healthdark.staffteam"
)
@SubCommandClasses(value = { StaffTeamCommands.ModifyTeamSubCommand.class, StaffTeamCommands.CreateTeamSubCommand.class, StaffTeamCommands.DeleteTeamSubCommand.class})
public class StaffTeamCommands implements CommandClass {

    @Inject
    private TeamManager teamManager;

    @Inject
    private UhcTeamMenu uhcTeamMenu;

    @Inject
    private PlayerManager playerManager;

    @Inject
    private ModeManager modeManager;

    @Inject
    private GameManager gameManager;

    @Command(
            names = "size"
    )
    public void sizeCommand(@Sender Player sender, @Named("size") int size) {
        if(size < 0) {
            sender.sendMessage(ChatUtils.PREFIX + "Los teams no pueden tener este tamaño.");
            return;
        }

        teamManager.setTeamSize(size);

        sender.sendMessage(ChatUtils.PREFIX + "El tamaño de equipos es: " + teamManager.getFormatTeamSize());
    }


    // MEMBER SUBCOMMAND
    @Command(names = "member")
    public class MemberTeamSubCommand implements CommandClass {
        @Command(
                names = "join"
        )
        public void joinCommand(@Sender Player sender, @Named("joinMember") Player target) {
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
        public void leaveCommand(@Sender Player sender, @Named("leaveMember") Player target) {
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
    }




    // DELETE SUBCOMMAND
    @Command(names = "delete")
    public class DeleteTeamSubCommand implements CommandClass {
        @Command(
                names = "player"
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
                names = "name"
        )
        public void deleteWithNameCommand(@Sender Player sender, @Text @Named("teamName") String text) {
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

        @Command(names = "all")
        public void deleteAllCommand(@Sender Player sender) {
            if(sender != gameManager.getHost()) {
                sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "No eres el host de la partida"));
                return;
            }

            for(var uhcTeam : teamManager.getUhcTeams().values()) {
                Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(uhcTeam));
            }

            Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + String.format("%s borró todos los teams de la partida",sender.getName())));
        }
    }


    // CREATE SUBCOMMAND
    @Command(names = "create")
    public class CreateTeamSubCommand implements CommandClass {
        @Command(
                names = "team"
        )
        public void createCommand(@Sender Player sender,@Named("teamowner") Player target, @Text @OptArg @Named("teamname") String name) {
            var teamPlayer = teamManager.getTeam(target.getUniqueId());

            if (teamPlayer != null) {
                sender.sendMessage(ChatUtils.PREFIX + "El usuario ya tiene un equipo.");
                return;
            }

            teamManager.createTeam(target);

            if(name != null) {
                var team = teamManager.getTeam(target.getUniqueId());

                team.setName(name);

                team.setPrefix(name);

                team.setBlockChangeName(true);
            }

            Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getTeam(target.getUniqueId()), target));
        }

        @Command(
                names = "random"
        )
        public void randomCommand(@Sender Player sender) {
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
        @Command(names = "chosen")
        public void chosenCommand(@Sender Player sender) {
            int teamSize = teamManager.getTeamSize();
            int toSelect = Bukkit.getOnlinePlayers().size() / teamSize;

            if(!modeManager.isActiveMode("chosen")) {
                sender.sendMessage(ChatUtils.PREFIX + "No está activo el modo Chosen");
                return;
            }

            List<Player> players = new ArrayList<>();

            Bukkit.getOnlinePlayers().forEach(p -> players.add(p));

            Collections.shuffle(players, new Random(ThreadLocalRandom.current().nextInt(Integer.MAX_VALUE)));

            for(int i = 0; i < toSelect; i++) {
                Player player = players.get(i);

                teamManager.createTeam(player);

                Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getPlayerTeam(player.getUniqueId()), player));

                player.sendMessage(ChatUtils.TEAM + "Has sido elegido para elegir a tu team. Para invitar usa &6/team invite");
            }

            Bukkit.getOnlinePlayers().forEach(p -> {
                var uhcTeam = teamManager.getPlayerTeam(p.getUniqueId());

                if(uhcTeam == null) {
                    p.sendMessage(ChatUtils.PREFIX + "Ya han sido seleccionados los anfitriones, estos los estaran invitando a ustedes. Para aceptar una invitación usar &6/team accept");
                }
            });

        }
    }






    // MODIFY SUBCOMMAND
    @Command(names = "modify")
    public class ModifyTeamSubCommand implements CommandClass {
        @Command(names = "prefix")
        public void modifyPrefixCommand(@Sender Player sender, @Named("teamOwner") Player target, @Text @Named("newPrefix") String prefix) {
            var uhcTeam = teamManager.getPlayerTeam(target.getUniqueId());

            if (uhcTeam == null) {
                sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                return;
            }

            uhcTeam.setPrefix(prefix);

            sender.sendMessage(ChatUtils.PREFIX + "Has cambiado el prefix existosamente a " + prefix);

            uhcTeam.sendMessage(String.format("%s ha cambiado su prefix a %s", sender.getName(), prefix));
        }

        @Command(names = "color")
        public void modifyColorCommand(@Sender Player sender, @Named("teamOwner") Player target, @Named("color") String color) {
            var uhcTeam = teamManager.getPlayerTeam(target.getUniqueId());

            if (uhcTeam == null) {
                sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                return;
            }

            String s = color;

            ChatColor cColor;


            try {
             cColor = ChatColor.valueOf(s);
            } catch (Exception e) {
                sender.sendMessage(ChatUtils.PREFIX + "Ese color no existe.");
                return;
            }



            uhcTeam.getTeam().setColor(cColor);

            sender.sendMessage(ChatUtils.PREFIX + "Has cambiado el color del team a " + color.toUpperCase());

            uhcTeam.sendMessage(String.format("%s ha cambiado el color del team a %s", sender.getName(), color.toUpperCase()));
        }
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
