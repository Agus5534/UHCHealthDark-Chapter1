package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

public class PlayerScatteredListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private PlayerManager playerManager;

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

        player.setExp(0);
        player.setLevel(0);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 9));
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, Integer.MAX_VALUE, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 4));
        player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, Integer.MAX_VALUE, 4));

        player.teleport(e.getLocation());
    }
}
