package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.entities.EntityPersistentData;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "super_heroes",
        name = "&bSuper Heroes",
        material = Material.DIAMOND,
        lore = {
                "&7- Al iniciar la partida recibirás un buff de los siguientes: ",
                "&7- Speed II & Night Vision I",
                "&7- Jump Boost III & Speed I",
                "&7- +50% de Vida & Resistance I",
                "&7- Resistance I & Fire Resistance I",
                "&7- Dolphin's Grace I & Water Breathing I & Luck II & Haste I"
        }
)
public class SuperHeroesMode extends Modality {

    @Inject
    private Main plugin;

    @Inject
    private GameManager gameManager;

    private List<String> tags;

    public SuperHeroesMode() throws IllegalClassFormatException {
        super();
    }

    @Override
    public void activeMode() {
        super.activeMode();

        String[] tagArr = new String[]{
                "movement",
                "jump",
                "tank",
                "fire",
                "water"
        };

        tags = Arrays.asList(tagArr);
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Bukkit.getOnlinePlayers().forEach(player -> Bukkit.getScheduler().runTaskLater(plugin, () -> giveSuper(player), 200L));

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()->applyEffect(), 220L, 18L);
    }

    @EventHandler
    public void onPlayerLaterScatter(PlayerLaterScatterEvent e) {
        var player = e.getPlayer();

        giveSuper(player);
    }

    private void giveSuper(Player player) {
        String s = tags.get(new Random().nextInt(tags.size()));

        EntityPersistentData entityPersistentData = new EntityPersistentData(plugin, "power", player);
        entityPersistentData.setData(PersistentDataType.STRING, s);
    }

    private void applyEffect() {
        Bukkit.getOnlinePlayers().forEach(player -> {
            EntityPersistentData persistentData = new EntityPersistentData(plugin, "power", player);

            if(persistentData == null) { return; }

            if(!persistentData.hasData(PersistentDataType.STRING)) { return; }

            if(persistentData.getData(PersistentDataType.STRING) == null) { return; }

            String s = (String) persistentData.getData(PersistentDataType.STRING);

            switch (s) {
                case "movement" -> {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 219, 1, true, false, true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.NIGHT_VISION, 219, 0, true, false, true));
                }
                case "jump" -> {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 219, 2, true, false, true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 219, 0, true, false, true));
                }
                case "tank" -> {
                    var hp = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);

                    if(hp.getBaseValue() != 30.0D) {
                        player.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30.0D);
                        player.setHealth(30.0D);
                    }

                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 219, 0, true, false, true));
                }
                case "fire" -> {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 219, 0, true, false, true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 219, 0, true, false, true));
                }
                case "water" -> {
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 219, 0, true, false, true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.WATER_BREATHING, 219, 0, true, false, true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.LUCK, 219, 1, true, false, true));
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 219, 1, true, false, true));
                }
            }
        });
    }
}
