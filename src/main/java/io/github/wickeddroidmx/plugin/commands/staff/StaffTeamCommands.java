package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.agus5534.hdbot.minecraft.events.ThreadMessageLogEvent;
import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.PlayerLeaveTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamDeleteEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.menu.UhcTeamMenu;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.*;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Command(
        names="staffteam",
        permission = "healthdark.staffteam"
)
@SubCommandClasses(value = { StaffTeamCommands.ModifyTeamSubCommand.class, StaffTeamCommands.CreateTeamSubCommand.class, StaffTeamCommands.DeleteTeamSubCommand.class, StaffTeamCommands.MemberTeamSubCommand.class})
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
    @Inject
    private Main plugin;

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

        @Command(
                names = "kill"
        )
        public void killCommand(@Sender Player sender, @Named("killPlayer") Player target, @Named("ban") boolean ban) {
            var uhcPlayer = playerManager.getPlayer(target.getUniqueId());

            if(uhcPlayer == null) {
                sender.sendMessage(ChatUtils.formatComponentPrefix("El usuario no es un UHC Player"));
                return;
            }

            if(!uhcPlayer.isAlive() || uhcPlayer.isSpect()) {
                sender.sendMessage(ChatUtils.formatComponentPrefix("El usuario no está vivo"));
                return;
            }

            target.chat("/tl");
            target.setHealth(0.0D);

            Bukkit.getScheduler().runTaskLater(plugin, ()-> {
                if(ban) {
                    target.banPlayer(ChatColor.RED + "¡Has sido descalificado!");
                } else {
                    target.kick(ChatUtils.formatC("&4¡Has sido descalificado!"));
                }

                Bukkit.broadcast(ChatUtils.formatComponentPrefix(String.format("El usuario %s ha sido descalificado de la partida.", target.getName())));

                Bukkit.getPluginManager().callEvent(new ThreadMessageLogEvent("Descalificación", String.format("%s ha sido descalificado", target.getName()), ThreadMessageLogEvent.EMBED_TYPE.CRITICAL, gameManager.getUhcId()));
            },20L);

        }

        @Command(names = "revive")
        public void reviveCommand(@Sender Player sender, @Named("toRevive") Player target, @Named("recoverInv") Boolean setInv, @Named("health") int health) {
            var uhcPlayer = playerManager.getPlayer(target.getUniqueId());
            var uhcTeam = teamManager.getPlayerTeam(target.getUniqueId());

            if(gameManager.getGameState() == GameState.WAITING) {
                sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7La partida no ha iniciado."));
                return;
            }

            if(gameManager.getGameState() == GameState.MEETUP) {
                sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7Este comando no se puede utilizar durante el Meetup."));
                return;
            }

            if(uhcPlayer == null) {
                sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7Esta persona no juega la partida."));
                return;
            }

            if(uhcTeam == null) {
                sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7La data de esta persona ha sido eliminada (Equipo eliminado definitivamente)."));
                return;
            }

            if(health < 1) {
                sender.sendMessage(ChatUtils.PREFIX + "La vida no puede ser menor a 1.");
                return;
            }

            if(health > uhcPlayer.getPlayer().getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue()) {
                sender.sendMessage(ChatUtils.PREFIX + "La vida no puede ser menor a la de los contenedores de esta persona.");
                return;
            }

            var uhcInv = uhcPlayer.getUhcInventory();
            var inv = uhcInv.getInventory();
            var loc = uhcInv.getLocation();

            if(inv == null || loc == null) {
                sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7Esta persona nunca ha muerto."));
                return;
            }

            uhcTeam.incrementPlayersAlive();
            target.getInventory().clear();

            var killer = target.getKiller();
            if(killer != null) {
                var killerPlayer = playerManager.getPlayer(killer.getUniqueId());
                var teamKiller = teamManager.getPlayerTeam(killer.getUniqueId());

                if(killerPlayer != null) {
                    killerPlayer.setKills(killerPlayer.getKills()-1);
                }

                if(teamKiller != null) {
                    teamKiller.setKills(teamKiller.getKills()-1);
                }

            }

            uhcPlayer.setAlive(true);
            uhcPlayer.setDeath(false);
            target.setGameMode(GameMode.SURVIVAL);
            uhcTeam.getTeam().addEntry(target.getName());


            if(setInv) {
                for(var i : inv.keySet()) {
                    if(inv.get(i) == null) { continue; }

                    target.getInventory().setItem(i, inv.get(i));
                }

            }

            target.teleport(loc);

            target.getActivePotionEffects().forEach(potionEffect -> target.removePotionEffect(potionEffect.getType()));
            target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 10, false,false,false));

            Bukkit.broadcastMessage(ChatUtils.PREFIX + ChatUtils.format("Se ha revivido al jugador &6"+target.getName()));

            uhcPlayer.getPlayer().setHealth(health);
        }
    }




    // DELETE SUBCOMMAND
    @Command(names = "delete")
    public class DeleteTeamSubCommand implements CommandClass {
        @Command(
                names = "player"
        )
        public void teamDeleteCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam) {
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
        public void createCommand(@Sender Player sender, @Named("teamowner") Player target, @Text @OptArg @Named("teamname") String name) {
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
        @Command(names = "captains")
        public void captainsCommand(@Sender Player sender) {
            int teamSize = teamManager.getTeamSize();
            int toSelect = Bukkit.getOnlinePlayers().size() / teamSize;

            if(!modeManager.isActiveMode("captains")) {
                sender.sendMessage(ChatUtils.PREFIX + "No está activo el modo Captains");
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
        public void modifyPrefixCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Text @Named("newPrefix") String prefix) {

            if (uhcTeam == null) {
                sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                return;
            }

            uhcTeam.setPrefix(prefix);

            sender.sendMessage(ChatUtils.PREFIX + "Has cambiado el prefix existosamente a " + prefix);

            uhcTeam.sendMessage(String.format("%s ha cambiado su prefix a %s", sender.getName(), prefix));
        }

        @Command(names = "color")
        public void modifyColorCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Named("color") ChatColor color) {
            if (uhcTeam == null) {
                sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                return;
            }

            uhcTeam.getTeam().setColor(color);

            uhcTeam.setColor(color);

            sender.sendMessage(ChatUtils.PREFIX + "Has cambiado el color del team a " + color.name());

            uhcTeam.sendMessage(String.format("%s ha cambiado el color del team a %s", sender.getName(), color.name()));
        }

        @Command(names = "name")
        public void modifyNameCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Named("name") @Text String name) {
            if (uhcTeam == null) {
                sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                return;
            }

            Bukkit.broadcast(Component.text(ChatUtils.PREFIX + String.format("El equipo %s ha sido renombrado a %s", uhcTeam.getName(), name)));

            uhcTeam.setName(name);
        }

        @Command(names = "owner")
        public void modifyOwnerCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Named("newOwner") Player newOwner) {
            if (uhcTeam == null) {
                sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                return;
            }

            if(!uhcTeam.getTeamPlayers().contains(newOwner.getUniqueId())) {
                sender.sendMessage(ChatUtils.PREFIX + "Ese jugador no pertenece a ese equipo.");
                return;
            }

            sender.sendMessage(ChatUtils.PREFIX + "Has transferido exitosamente el owner del equipo a " + newOwner.getName());

            uhcTeam.sendMessage(String.format("%s ha transferido la propiedad del equipo de %s a %s",
                    sender.getName(),
                    uhcTeam.getOwner().getName(),
                    newOwner.getName()));

            uhcTeam.setOwner(newOwner);
        }

        @Command(names = "flag")
        public void flagCommand(@Sender Player sender, @Named("teamOwner") UhcTeam uhcTeam, @Named("flag") TeamFlags flag, @Named("newValue") boolean value) {
            if (uhcTeam == null) {
                sender.sendMessage(ChatUtils.PREFIX + "No existe ese equipo");
                return;
            }

            if(uhcTeam.containsFlag(flag) == value) {
                sender.sendMessage(ChatUtils.PREFIX + "La flag ya está en ese valor");
                return;
            }

            if(value) {
                uhcTeam.addFlag(flag);
            } else {
                uhcTeam.removeFlag(flag);
            }

            uhcTeam.sendMessage(String.format("La flag del equipo '%s' ha cambiado a %s", flag.getName(), value));
            sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX +  String.format("Has cambiado la flag '%s' a %s", flag.getName(), value)));
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
