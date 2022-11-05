package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Wolf;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Random;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        key = "regret_ful_wolfs",
        name = "&cRegret Ful Wolfs",
        material = Material.WOLF_SPAWN_EGG,
        lore = {
                "&7- Cuando matas a alguien, aparece un lobo con alguno de estos efectos:",
                "&7- Strength I, Speed I, Resistance I"
        }
)
public class RegretFulWolfsScenario extends Modality {
    public RegretFulWolfsScenario() throws IllegalClassFormatException {
        super();
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
