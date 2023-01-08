package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.agus5534.hdbot.minecraft.events.ThreadMessageLogEvent;
import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.events.team.TeamDeleteEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.player.DeathType;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.yushust.inject.InjectAll;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Named;
import java.util.UUID;

@InjectAll
public class PlayerDeathListener implements Listener {

    private PlayerManager playerManager;
    private TeamManager teamManager;
    private GameManager gameManager;
    private Main plugin;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());
        var uhcTeam = teamManager.getPlayerTeam(player.getUniqueId());
        var deathType = gameManager.getDeathType();


        if (gameManager.getGameState() == GameState.WAITING)
            return;


        Bukkit.getPluginManager().callEvent(
                new ThreadMessageLogEvent(
                        String.format("Muerte de %s", player.getName()),
                        e.getDeathMessage(),
                        ThreadMessageLogEvent.EMBED_TYPE.DEATH,
                        gameManager.getUhcId()
                )
        );

        var location = e.getEntity().getLocation();

        Bukkit.getLogger().info(String.format("%s murió en X: %d Y: %d Z: %d en %s",
                player.getName(),
                Math.round(location.getX()),
                Math.round(location.getY()),
                Math.round(location.getZ()),
                location.getWorld().getName()));

        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            player.spigot().respawn();

            if(deathType == DeathType.INSTANT_SPECTATE) {
                e.getDrops().clear();
                //player.teleport(location);
            }

            if(deathType == DeathType.NORMAL) {
                player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
            }

            if(gameManager.isSpectators()) {
                player.setGameMode(GameMode.SPECTATOR);
            } else {
                if(deathType == DeathType.NORMAL) {
                    player.setGameMode(GameMode.ADVENTURE);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 15, false, false, false));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SATURATION, Integer.MAX_VALUE, 20, false, false, false));
                }
            }

        }, 5L);

        if (player.getKiller() != null) {
            var killer = player.getKiller();
            var uhcKiller = playerManager.getPlayer(killer.getUniqueId());
            var teamKiller = teamManager.getPlayerTeam(killer.getUniqueId());

            uhcKiller.incrementKills();

            if (teamKiller != null)
                teamKiller.incrementKills();
        }

        if (uhcTeam != null) {
            uhcTeam.decrementPlayersAlive();

            if (uhcTeam.getPlayersAlive() == 0) {
                Bukkit.broadcast(Component.text(ChatUtils.PREFIX + String.format("El equipo %s ha muerto.", uhcTeam.getName())));

                Bukkit.getPluginManager().callEvent(new TeamDeleteEvent(uhcTeam));
            }
        }

        if (uhcPlayer != null) {
            uhcPlayer.setAlive(false);

            uhcPlayer.getUhcInventory().setInventory(e.getEntity().getInventory());

            uhcPlayer.getUhcInventory().setLocation(e.getEntity().getLocation());

            Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F));

            gameManager.getSpectatorTeam().addEntry(player.getName());
        }


    }
}
