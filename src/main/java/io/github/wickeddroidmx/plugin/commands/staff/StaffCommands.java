package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
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

    @Command(
            names = "worldtp", permission = "healthdark.staff"
    )
    public void teleportCommand(@Sender Player sender, World world) {
        if(world == null) {
            sender.sendMessage(ChatUtils.PREFIX + "No existe ese mundo");
            return;
        }

        sender.teleport(world.getSpawnLocation());
        sender.sendMessage(ChatUtils.PREFIX + String.format("Te has teletransportado al mundo %s", world.getName()));
    }

    @Command(
            names = { "ls", "laterscatter", "revive" }, permission = "healthdark.staff"
    )
    public void laterScatterCommand(@Sender Player sender, Player target) {
        var uhcPlayer = playerManager.getPlayer(target.getUniqueId());
        var teamPlayer = teamManager.getPlayerTeam(target.getUniqueId());
        var random = new Random();


        if (uhcPlayer != null) {
            if (uhcPlayer.isAlive()){
                sender.sendMessage(ChatUtils.PREFIX + "El usuario ya esta vivo.");
                return;
            }

            if (teamPlayer == null) {
                teamManager.createTeam(target);

                Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getPlayerTeam(target.getUniqueId()), target));
            }

            target.teleport(new Location(Bukkit.getWorld("uhc_world"), random.nextInt(gameManager.getWorldBorder()), 100, random.nextInt(gameManager.getWorldBorder())));

            target.setGameMode(GameMode.SURVIVAL);
            target.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 200, 5));

            Bukkit.broadcastMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("%s scattered", target.getName())));

            Bukkit.getPluginManager().callEvent(new PlayerLaterScatterEvent(target, target.getLocation()));

            gameManager.getSpectatorTeam().removeEntry(target.getName());

            uhcPlayer.setAlive(true);
            uhcPlayer.setScattered(true);
            uhcPlayer.setDeath(false);
        }
    }

}
