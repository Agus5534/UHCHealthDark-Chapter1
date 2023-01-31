package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Arrays;
import java.util.Random;

@GameModality(
        name = "&6Damage Cycle",
        modalityType = ModalityType.MODE,
        material = Material.WOODEN_SWORD,
        key = "damage_cycle",
        experimental = true,
        lore = {"&7- Cada 15 minutos un daño hace x10"}
)
public class DamageCycleMode extends Modality {
    int taskID = -1234567;
    EntityDamageEvent.DamageCause damageCause = EntityDamageEvent.DamageCause.DRAGON_BREATH;

    @Inject
    private Main plugin;

    @Inject
    private GameManager gameManager;

    public DamageCycleMode() throws IllegalClassFormatException {
        super();
    }


    @Override
    public void deactivateMode() {
        super.deactivateMode();
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }

    @Override
    public void activeMode() {
        super.activeMode();

        if(gameManager.getGameState() != GameState.WAITING) {
            taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> changeDamageType(),20L,18000L);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if(event.getCause() == getDamageCause()) {
            event.setDamage(event.getDamage() * 10.0);
        }
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> changeDamageType(),100L, 18000L);
    }


    public void changeDamageType() {
        EntityDamageEvent.DamageCause[] availableDamages = new EntityDamageEvent.DamageCause[] {
                EntityDamageEvent.DamageCause.CONTACT,
                EntityDamageEvent.DamageCause.DROWNING,
                EntityDamageEvent.DamageCause.BLOCK_EXPLOSION,
                EntityDamageEvent.DamageCause.FALL,
                EntityDamageEvent.DamageCause.FIRE,
                EntityDamageEvent.DamageCause.FIRE_TICK,
                EntityDamageEvent.DamageCause.LAVA,
                EntityDamageEvent.DamageCause.HOT_FLOOR,
                EntityDamageEvent.DamageCause.LIGHTNING,
                EntityDamageEvent.DamageCause.WITHER,
                EntityDamageEvent.DamageCause.STARVATION,
                EntityDamageEvent.DamageCause.SUFFOCATION,
                EntityDamageEvent.DamageCause.ENTITY_ATTACK,
                EntityDamageEvent.DamageCause.ENTITY_EXPLOSION,
                EntityDamageEvent.DamageCause.PROJECTILE,
                EntityDamageEvent.DamageCause.THORNS
        };

        setDamageCause(
                Arrays.asList(availableDamages).stream()
                        .filter(damageCause1 -> damageCause1 != getDamageCause())
                        .toList().get(new Random().nextInt(Arrays.asList(availableDamages).size() - 1))
        );
    }

    public EntityDamageEvent.DamageCause getDamageCause() {
        return damageCause;
    }

    public void setDamageCause(EntityDamageEvent.DamageCause damageCause) {
        this.damageCause = damageCause;
        Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + "El daño se ha cambiado por "+ damageCause.toString()));
    }
}
