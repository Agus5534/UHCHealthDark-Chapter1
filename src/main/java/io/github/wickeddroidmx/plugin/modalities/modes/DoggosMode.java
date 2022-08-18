package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;

public class DoggosMode extends Modality {

    @Inject
    private Main plugin;

    @Inject
    private GameManager gameManager;

    public DoggosMode() {
        super(ModalityType.MODE, "doggos", "&fDoggos", Material.WOLF_SPAWN_EGG,
                ChatUtils.format("&7- Al inciar la partida se te dará un perro custom"),
                ChatUtils.format("&7- Al llegar a 0,0 y tener este perro se te dará un recompensa."));
    }

    @EventHandler
    public void onPlayerScatter(PlayerScatteredEvent e) {
        generateWolf(e.getPlayer());
    }

    @EventHandler
    public void onPlayerLateScatter(PlayerLaterScatterEvent e) {
        generateWolf(e.getPlayer());
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        var entity = e.getEntity();

        if (entity instanceof Wolf wolf) {
            var persistent = wolf.getPersistentDataContainer();

            if (persistent.has(new NamespacedKey(plugin, "doggos"), PersistentDataType.STRING)) {
                var data = persistent.get(new NamespacedKey(plugin, "doggos"), PersistentDataType.STRING);

                Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format("Ha muerto el lobo de &6" + data)));
            }
        }
    }

    private void generateWolf(Player player) {
        var wolf = player.getWorld().spawn(player.getLocation(), Wolf.class);
        var persistent = wolf.getPersistentDataContainer();

        wolf.setCustomName(ChatUtils.format("&6" + player.getName()));
        wolf.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, (20 * 200), 5));
        wolf.setOwner(player);

        if (!persistent.has(new NamespacedKey(plugin, "doggos"), PersistentDataType.STRING)) {
            persistent.set(new NamespacedKey(plugin, "doggos"), PersistentDataType.STRING, player.getName());
        }
    }
}
