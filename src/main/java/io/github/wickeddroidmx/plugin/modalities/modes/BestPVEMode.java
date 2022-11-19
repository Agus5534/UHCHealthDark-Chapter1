package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.List;

@GameModality(
        name = "&5Best PvE",
        key = "best_pve",
        modalityType = ModalityType.MODE,
        material = Material.SHIELD,
        experimental = true,
        lore = {
                "&7- Todos comienzan en una lista",
                "&7- La cuál son removidos al perder vida",
                "&7- Los que permanezcan, ganan 1 cora",
                "&7- Al matar a alguien, reingresas a la lista",
                "&7- &4Incompatible con UHC España, Tanks y King"
        }
)
public class BestPVEMode extends Modality {
    List<Player> playerList;
    double health;

    @Inject
    private Main plugin;

    @Inject
    private ModeManager modeManager;

    public BestPVEMode() throws IllegalClassFormatException {
        super();
    }

    @Override
    public void activeMode() {
        super.activeMode();

        playerList = new ArrayList<>();
        health = 20.0D;
    }

    @EventHandler
    public void onScatter(PlayerScatteredEvent event) {
        playerList.add(event.getPlayer());
    }

    @EventHandler
    public void onLaterScatter(PlayerLaterScatterEvent event) {
        playerList.add(event.getPlayer());
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
            if(modeManager.isActiveMode("best_pve")) {
                check();
            }
        }, 1L, 20L);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> {
            if(modeManager.isActiveMode("best_pve")) {
                health = health+2.0D;

                Bukkit.broadcast(ChatUtils.formatComponentPrefix("Se ha aumentado 1 corazon."));
            }
        }, 12000L, 12000L);
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }
        if(event.getDamage() == 0.0D) { return; }

        var player = (Player)event.getEntity();

        if(playerList.contains(player)) {
            playerList.remove(player);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if(!(event.getEntity() instanceof Player)) { return; }
        if(!((event.getDamager()) instanceof Player)) { return; }

        var victim = (Player)event.getEntity();
        var damager = (Player)event.getDamager();

        if(victim.getHealth() - event.getDamage() > 0) { return; }

        if(!playerList.contains(damager)) {
            playerList.add(damager);
        }
    }

    private void check() {
        playerList
                .stream()
                .filter(p -> p.isOnline())
                .forEach(p -> {
                    var h = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

                    if(health != h) {
                        p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(health);
                        p.setHealth(p.getHealth()+2.0);
                    }
                });
    }
}
