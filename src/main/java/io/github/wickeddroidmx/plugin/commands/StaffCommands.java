package io.github.wickeddroidmx.plugin.commands;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.menu.PlayerInventoryMenu;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.util.Random;

public class StaffCommands implements CommandClass {

    @Inject
    private PlayerManager playerManager;

    @Inject
    private TeamManager teamManager;

    @Inject
    private GameManager gameManager;

    @Inject
    private Main plugin;

    @Command(
            names = "worldtp"
    )
    public void teleportCommand(@Sender Player sender, World world) {
        if(!sender.hasPermission("healthdark.host")) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("&4Missing Permissions"));
            return;
        }

        if(world == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No existe ese mundo");
            return;
        }

        if(plugin.getWorldGenerator().getUhcWorld().isRecreatingWorld()) {
            sender.sendMessage(ChatUtils.PREFIX + "El mundo se está recreando, el comando se encuentra desactivado.");
            return;
        }

        sender.teleport(world.getSpawnLocation());
        sender.sendMessage(ChatUtils.PREFIX + String.format("Te has teletransportado al mundo %s", world.getName()));
    }

    @Command(
            names = "setmaxslots"
    )
    public void setMaxSlotsCommand(@Sender Player sender, @Named("slots") int slots) {
        if(!sender.hasPermission("healthdark.host")) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("&4Missing Permissions"));
            return;
        }

        int s = Bukkit.getMaxPlayers();
        if(slots < s || slots > 120) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("No se pueden poner esa cantidad de slots."));
            return;
        }



        gameManager.setMaxPlayerSize(slots);

        sender.sendMessage(ChatUtils.formatComponentPrefix("Has cambiado los slots disponibles a " + slots));
    }

    @Command(
            names = { "ls", "laterscatter", "revive" }
    )
    public void laterScatterCommand(@Sender Player sender, @Named("player") Player target, @OptArg @Named("team") UhcTeam uhcTeam) {
        if(!sender.hasPermission("healthdark.host")) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("&4Missing Permissions"));
            return;
        }

        var uhcPlayer = playerManager.getPlayer(target.getUniqueId());
        var teamPlayer = teamManager.getPlayerTeam(target.getUniqueId());
        var random = new Random();


        if (uhcPlayer != null) {
            if(uhcPlayer.isSpect()) {
                sender.sendMessage(ChatUtils.PREFIX + "El usuario es espectador");
                return;
            }

            if (uhcPlayer.isAlive()){
                sender.sendMessage(ChatUtils.PREFIX + "El usuario ya esta vivo.");
                return;
            }

            if (teamPlayer == null && uhcTeam == null) {
                teamManager.createTeam(target);

                Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getPlayerTeam(target.getUniqueId()), target));
            }

            if(uhcTeam != null) {
                Bukkit.getPluginManager().callEvent(new PlayerJoinedTeamEvent(uhcTeam, target));

                if(uhcTeam.getSpawnLocation() == null) {
                    target.teleport(new Location(plugin.getWorldGenerator().getUhcWorld().getWorld(), random.nextInt(gameManager.getWorldBorder()), 100, random.nextInt(gameManager.getWorldBorder())));
                } else {
                    target.teleport(uhcTeam.getSpawnLocation());
                }
            }

            if(uhcTeam == null) {
                target.teleport(new Location(plugin.getWorldGenerator().getUhcWorld().getWorld(), random.nextInt(gameManager.getWorldBorder()), 100, random.nextInt(gameManager.getWorldBorder())));
            }

            target.setGameMode(GameMode.SURVIVAL);
            target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 400, 15));

            Bukkit.broadcastMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("%s scattered", target.getName())));

            Bukkit.getPluginManager().callEvent(new PlayerLaterScatterEvent(target, target.getLocation()));

            gameManager.getSpectatorTeam().removeEntry(target.getName());

            uhcPlayer.setAlive(true);
            uhcPlayer.setScattered(true);
            uhcPlayer.setDeath(false);
            uhcPlayer.getPlayer().setFlying(false);
        }
    }

    @Command(
            names = {"inv","inventory","invsee"}
    )
    public void inventoryCommand(@Sender Player sender, Player target) {
        if(!sender.hasPermission("healthdark.host")) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("&4Missing Permissions"));
            return;
        }

        var senderUhcPlayer = playerManager.getPlayer(sender.getUniqueId());

        if(senderUhcPlayer != null) {
            if(senderUhcPlayer.isAlive()) {
                sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "Eres un jugador de la partida."));
                return;
            }
        }

        var uhcPlayer = playerManager.getPlayer(target.getUniqueId());

        if(uhcPlayer == null) {
            sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "Ese jugador no juega la partida."));
            return;
        }

        if(!uhcPlayer.isAlive() || uhcPlayer.isSpect()) {
            sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "Ese jugador no tiene inventario para ver"));
            return;
        }

        var playerInv = new PlayerInventoryMenu(target);

        playerInv.openInv(sender);
    }

    @Command(
            names = {"invti", "invseeti", "tisee"}
    )
    public void teamInventorySeeCommand(@Sender Player sender, @Named("team") UhcTeam uhcTeam) {
        if(!sender.hasPermission("healthdark.staff.admin")) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("&4Missing Permissions"));
            return;
        }
        var senderUhcPlayer = playerManager.getPlayer(sender.getUniqueId());

        if(senderUhcPlayer != null) {
            if(senderUhcPlayer.isAlive() || !senderUhcPlayer.isSpect()) {
                sender.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "Eres un jugador de la partida."));
                return;
            }
        }

        var inv = uhcTeam.getInventory();

        if(inv == null) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("Ese team no tiene Team Inventory"));
            return;
        }

        if(sender.getGameMode() != GameMode.SPECTATOR) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("No estas en espectador"));
            return;
        }

        sender.openInventory(inv);
    }

    @Command(
            names = {"spect", "spectate", "moderate", "mod"}
    )
    public void spectateCommand(@Sender Player sender) {
        if(!sender.hasPermission("healthdark.staff.mod")) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("&4Missing Permissions"));
            return;
        }
        var player = playerManager.getPlayer(sender.getUniqueId());

        if(player == null) {
            playerManager.createPlayer(sender,false);

            player = playerManager.getPlayer(sender.getUniqueId());
        }

        if(teamManager.getPlayerTeam(sender.getUniqueId()) != null) {
            sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7¡Ya tienes un equipo!"));

            return;
        }

        if(player.isSpect()) {
            player.setSpect(false);

            sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7¡Ya no eres espectador!"));

            gameManager.getSpectatorTeam().removeEntry(sender.getName());
        } else {
            player.setSpect(true);

            sender.sendMessage(ChatUtils.PREFIX + ChatUtils.format("&7¡Ahora eres espectador!"));

            sender.setGameMode(GameMode.SPECTATOR);

            gameManager.getSpectatorTeam().addEntry(sender.getName());
        }
    }

}
