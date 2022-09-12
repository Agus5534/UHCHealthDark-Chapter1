package io.github.wickeddroidmx.plugin.listeners.worldborder;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderMoveEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.inject.Inject;

public class WorldBorderMoveListener implements Listener {

    @Inject
    private Main plugin;

    @Inject
    private GameManager gameManager;

    @EventHandler
    public void onWorldBorderChange(WorldBorderMoveEvent e) {
        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendTitle(ChatUtils.format("&e⚠ &6Borde Moviendose &e⚠"), ChatUtils.format(String.format("&7Borde &6%s&7x&6%s", e.getWorldBorder(), e.getWorldBorder())), 20, 60, 20);
            player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("El borde tardará &6%s segundos &7en llegar a &6%s&7x&6%s", e.getSeconds(), e.getWorldBorder(), e.getWorldBorder())));

            player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0F, 1.0F);
        });

        var world = Bukkit.getWorld("uhc_world");

        if (world != null) {
            var worldBorder = world.getWorldBorder();

            worldBorder.setSize((e.getWorldBorder() * 2), e.getSeconds());
            Bukkit.getScheduler().runTaskLater(plugin, () -> worldBorder.setDamageAmount(1.0f), (e.getSeconds() * 20L));
        }

        if(gameManager.isRunMode() && gameManager.getGameState() == GameState.MEETUP) {
            var nether = Bukkit.getWorlds().get(1);

            var wb = nether.getWorldBorder();

            wb.setSize(1, 420);

            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                wb.setDamageAmount(1.0);

                Bukkit.getOnlinePlayers().forEach(p -> {
                    if(p.getWorld().equals(nether) && p.getGameMode() == GameMode.SURVIVAL) {
                        var event = new EntityDamageEvent(p, EntityDamageEvent.DamageCause.CRAMMING, 100000);

                        Bukkit.getPluginManager().callEvent(event);

                        event.getEntity().setLastDamageCause(event);

                        p.setHealth(0);
                    }
                });
            }, 8401);
        }

    }
}
