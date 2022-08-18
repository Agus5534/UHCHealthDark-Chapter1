package io.github.wickeddroidmx.plugin.listeners.entities;

import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.sql.model.User;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

public class EntityDamageListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private ModeManager modeManager;

    @Inject
    @Named("ironman-cache")
    private ListCache<UUID> ironManCache;

    @Inject
    @Named("user-cache")
    private MapCache<UUID, User> userCache;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        var entity = e.getEntity();

        if (entity instanceof Player player) {
            if (gameManager.getGameState() == GameState.WAITING || gameManager.getGameState() == GameState.FINISHING) {
                e.setCancelled(true);
                return;
            }

            if (player.getGameMode() == GameMode.SPECTATOR || player.getGameMode() == GameMode.CREATIVE) {
                e.setCancelled(true);
                return;
            }

            if (player.isBlocking() && e.getDamage() != 0) {
                return;
            }

            if (modeManager.isActiveMode("no_fall") && e.getCause() == EntityDamageEvent.DamageCause.FALL)
                return;

            if (modeManager.isActiveMode("fire_less") && (e.getCause() == EntityDamageEvent.DamageCause.FIRE
                    || e.getCause() ==  EntityDamageEvent.DamageCause.HOT_FLOOR
                    || e.getCause() ==  EntityDamageEvent.DamageCause.LAVA
                    || e.getCause() ==  EntityDamageEvent.DamageCause.LAVA))
                return;

            var resistance = player.getPotionEffect(PotionEffectType.DAMAGE_RESISTANCE);
            var uuid = player.getUniqueId();

            if (resistance != null && resistance.getAmplifier() >= 4) {
                return;
            }


            if (ironManCache.exists(uuid)) {
                Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format(String.format("El usuario &6%s &7ha perdido el ironman.", player.getName()))));

                ironManCache.remove(uuid);
            }

            if (ironManCache.size() == 1) {
                var ironMan = ironManCache.remove(0);
                var ironPlayer = Bukkit.getOfflinePlayer(ironMan);

                Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format(String.format("El usuario &6%s &7ha sido el ironman de la partida.", ironPlayer.getName()))));

                if (userCache.exists(ironPlayer.getUniqueId()))
                    userCache.get(ironPlayer.getUniqueId()).incrementIronMans();


                ironManCache.remove(uuid);

                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0F));
            }
        } else if (e.getEntityType() == EntityType.DROPPED_ITEM) {
            e.setCancelled(true);
        }
    }
}
