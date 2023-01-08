package io.github.wickeddroidmx.plugin.listeners.entities;

import io.github.agus5534.hdbot.minecraft.events.ThreadMessageLogEvent;
import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.cache.MapCache;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.player.DeathType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.*;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.util.Vector;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Arrays;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

public class EntityDamageListener implements Listener {

    @Inject
    private GameManager gameManager;

    @Inject
    private ModeManager modeManager;

    @Inject
    private Main plugin;
    @Inject
    @Named("ironman-cache")
    private ListCache<UUID> ironManCache;

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        var entity = e.getEntity();

        if (entity instanceof Player player) {

            if(player.getHealth() - e.getFinalDamage() <= 0.0D && gameManager.getDeathType() == DeathType.INSTANT_SPECTATE) {
                Bukkit.getScheduler().runTask(plugin, ()-> {
                    boolean drop = !(modeManager.isActiveMode("grave_robbers"));

                    player.setGameMode(GameMode.SPECTATOR);
                    player.setHealth(player.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

                    player.setBedSpawnLocation(player.getLocation(), true);

                    if(drop) {
                        Arrays.stream(player.getInventory().getContents()).forEach(i -> this.drop(i, player.getLocation(), player.getEyeHeight()));
                        Arrays.stream(player.getInventory().getArmorContents()).forEach(i -> this.drop(i, player.getLocation(), player.getEyeHeight()));
                    }
                });
            }

            if (gameManager.getGameState() == GameState.WAITING || gameManager.getGameState() == GameState.FINISHING) {
                if(!plugin.getARENA().isInsideRegion(entity.getLocation())) {
                    if(e.getCause() == EntityDamageEvent.DamageCause.VOID) {
                        player.teleport(Bukkit.getWorlds().get(0).getSpawnLocation());
                    }

                    e.setCancelled(true);
                    return;
                }
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

                Bukkit.getPluginManager().callEvent(
                        new ThreadMessageLogEvent(
                                String.format("> %s ya no puede ser ironman", player.getName()),
                                gameManager.getUhcId()
                        )
                );
            }

            if (ironManCache.size() == 1) {
                var ironMan = ironManCache.remove(0);
                var ironPlayer = Bukkit.getOfflinePlayer(ironMan);

                Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format(String.format("El usuario &6%s &7ha sido el ironman de la partida.", ironPlayer.getName()))));

                ironManCache.remove(uuid);

                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> onlinePlayer.playSound(onlinePlayer.getLocation(), Sound.BLOCK_ANVIL_USE, 1.0f, 1.0F));


                Bukkit.getPluginManager().callEvent(
                        new ThreadMessageLogEvent(
                                String.format("> %s es el ironman de la partida", ironPlayer.getName()),
                                gameManager.getUhcId()
                        )
                );

            }
        }
    }

    private void drop(ItemStack itemStack, Location playerLoc, double eyeHeight) {

        if(itemStack == null) { return; }
        if(itemStack.getType() == Material.AIR) { return; }

        double d0 = playerLoc.getY() + eyeHeight - (double)0.3F;
        var spawnLoc = new Location(playerLoc.getWorld(), playerLoc.getX(), d0, playerLoc.getZ());

        Random random = new Random();
        float f7 = 0.3F;
        float f8 = (float) Math.sin(playerLoc.getYaw() * ((float)Math.PI / 180F));
        float f2 = (float) Math.cos(playerLoc.getYaw() * ((float)Math.PI / 180F));
        float f3 = (float) Math.sin(playerLoc.getPitch() * ((float)Math.PI / 180F));
        float f4 = (float) Math.cos(playerLoc.getPitch() * ((float)Math.PI / 180F));
        float f5 = random.nextFloat() * ((float)Math.PI * 2F);
        float f6 = 0.02F * random.nextFloat();
        var item = playerLoc.getWorld().dropItem(spawnLoc, itemStack);

        item.setVelocity(getVector((double)(-f3 * f2 * 0.3F) + Math.cos(f5) * (double)f6, (double)(-f8 * 0.3F + 0.1F + (random.nextFloat() - random.nextFloat()) * 0.1F), (double)(f4 * f2 * 0.3F) + Math.sin(f5) * (double)f6));

    }

    private org.bukkit.util.Vector getVector(double d1, double d2, double d3) {
        return new Vector(d1, d2, d3);
    }
}
