package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.events.team.TeamDeleteEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.hooks.DiscordWebhook;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.sql.model.User;
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

import javax.inject.Named;
import java.awt.Color;
import java.io.IOException;
import java.util.UUID;

@InjectAll
public class PlayerDeathListener implements Listener {

    private PlayerManager playerManager;
    private TeamManager teamManager;
    private GameManager gameManager;

    @Named("user-cache")
    private MapCache<UUID, User> userCache;

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());
        var uhcTeam = teamManager.getPlayerTeam(player.getUniqueId());

        if (gameManager.getGameState() == GameState.WAITING)
            return;

        var hook = new DiscordWebhook("https://discord.com/api/webhooks/920007596004474930/O3f90OX8H6z3Vhqgh-AEvXFwDNzNZfLV9CwmYYrjIniSzFUKcrrVREvhZpdbZ4QYisla");

        hook.addEmbed(
                new DiscordWebhook.EmbedObject()
                        .setTitle("Muerte de " + player.getName())
                        .setDescription(e.getDeathMessage())
                        .setColor(Color.RED)
        );

        try {
            hook.execute();
        } catch (IOException err) {
            err.printStackTrace();
        }

        var location = e.getEntity().getLocation();

        Bukkit.getLogger().info(String.format("%s murió en X: %d Y: %d Z: %d en %s",
                player.getName(),
                Math.round(location.getX()),
                Math.round(location.getY()),
                Math.round(location.getZ()),
                location.getWorld().getName()));

        if (player.getKiller() != null) {
            var killer = player.getKiller();
            var uhcKiller = playerManager.getPlayer(killer.getUniqueId());
            var teamKiller = teamManager.getPlayerTeam(killer.getUniqueId());
            var user = userCache.get(killer.getUniqueId());

            uhcKiller.incrementKills();
            user.incrementKills();

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

            player.setGameMode(GameMode.SPECTATOR);
            gameManager.getSpectatorTeam().addEntry(player.getName());
        }


    }
}
