package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;

public class RegretFulWolfsScenario extends Modality {
    public RegretFulWolfsScenario() {
        super(ModalityType.SCENARIO, "regret_ful_wolfs", "&cRegret Ful Wolfs", Material.WOLF_SPAWN_EGG,
                ChatUtils.format("&7- Cuando matas a alguien aparecé un lobo con alguno de estos efectos:"),
                ChatUtils.format("&7- Strength 1, Speed 1, Resistance 1"));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        var killer = player.getKiller();
        var random = new Random();

        if (killer != null) {
            var wolf = player.getWorld().spawn(player.getLocation(), Wolf.class);
            wolf.setOwner(killer);

            switch (random.nextInt(3) + 1) {
                case 1 -> {
                    wolf.setCustomName(ChatUtils.format("&cStrength Wolf"));
                    wolf.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));

                }
                case 2 -> {
                    wolf.setCustomName(ChatUtils.format("&bSpeed Wolf"));
                    wolf.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
                }
                case 3 -> {
                    wolf.setCustomName(ChatUtils.format("&7Resistance Wolf"));
                    wolf.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, Integer.MAX_VALUE, 0));
                }
            }
        }
    }
}
