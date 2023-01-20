package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.agus5534.hdbot.minecraft.events.ThreadMessageLogEvent;
import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Iterator;
import java.util.UUID;

public class PlayerScatteredListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerManager playerManager;

    @Inject
    private TeamManager teamManager;

    @Inject
    @Named("ironman-cache")
    private ListCache<UUID> ironManCache;

    @EventHandler
    public void onPlayerScattered(PlayerScatteredEvent e) {
        var player = e.getPlayer();

        if (!ironManCache.exists(player.getUniqueId()))
            ironManCache.add(player.getUniqueId());

        if (!playerManager.existsPlayer(player)) {
            playerManager.createPlayer(player, true);
        }

        Iterator<Advancement> advancements = Bukkit.getServer().advancementIterator();

        while (advancements.hasNext()) {
            AdvancementProgress progress = player.getAdvancementProgress(advancements.next());
            for (String s : progress.getAwardedCriteria())
                progress.revokeCriteria(s);
        }

        player.setExp(0);
        player.closeInventory();
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().addItem(new ItemCreator(Material.ACACIA_BOAT).amount(1));

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 9));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 4));

        player.teleport(e.getLocation());

        Bukkit.getLogger().info(String.format("Scattered player %s to X: %d Y: %d Z: %d in world %s",
                player.getName(),
                Math.round(e.getLocation().getX()),
                Math.round(e.getLocation().getY()),
                Math.round(e.getLocation().getZ()),
                e.getLocation().getWorld().getName()));

        var team = teamManager.getPlayerTeam(e.getPlayer().getUniqueId());

        if(team == null) { return; }
        if(team.getSpawnLocation() == null) {
            team.setSpawnLocation(e.getLocation());
        }
    }

    @EventHandler
    public void onLaterScatter(PlayerLaterScatterEvent e) {
        var player = e.getPlayer();

        Iterator<Advancement> advancements = Bukkit.getServer().advancementIterator();

        while (advancements.hasNext()) {
            AdvancementProgress progress = player.getAdvancementProgress(advancements.next());
            for (String s : progress.getAwardedCriteria())
                progress.revokeCriteria(s);
        }

        player.setExp(0);
        player.closeInventory();
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();
        player.getInventory().addItem(new ItemCreator(Material.ACACIA_BOAT).amount(1));

        player.getInventory().addItem(new ItemCreator(Material.ACACIA_BOAT).amount(1));
        Bukkit.getLogger().info(String.format("Scattered player %s to X: %d Y: %d Z: %d in world %s",
                player.getName(),
                Math.round(e.getLocation().getX()),
                Math.round(e.getLocation().getY()),
                Math.round(e.getLocation().getZ()),
                e.getLocation().getWorld().getName()));


        Bukkit.getPluginManager().callEvent(
                new ThreadMessageLogEvent(
                        "Late Scatter",
                        String.format("El jugador %s fue scattereado", player.getName()),
                        ThreadMessageLogEvent.EMBED_TYPE.BLANK,
                        gameManager.getUhcId()
                )
        );


        var team = teamManager.getPlayerTeam(e.getPlayer().getUniqueId());

        if(team == null) { return; }
        if(team.getSpawnLocation() == null) {
            team.setSpawnLocation(e.getLocation());
        }
    }
}
