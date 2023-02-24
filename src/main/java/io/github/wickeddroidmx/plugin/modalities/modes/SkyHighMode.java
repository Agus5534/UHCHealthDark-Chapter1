package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "skyhigh",
        name = "&bUHC SkyHigh",
        material = Material.ELYTRA,
        lore = {"&7- PvP en capa +150"}
)
public class SkyHighMode extends Modality {
    @Inject
    private Main plugin;
    @Inject
    private GameManager gameManager;

    int taskID;

    public SkyHighMode() throws IllegalClassFormatException {
        super();
    }


    @Override
    public void activeMode() {
        super.activeMode();
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> check(), 2L, 2L);
    }

    @Override
    public void deactivateMode() {
        super.deactivateMode();
        Bukkit.getScheduler().cancelTask(taskID);
    }

    private void check() {
        if(gameManager.getGameState() != GameState.MEETUP) {
            return;
        }

        if(!this.isEnabled()) { return; }

        Bukkit.getOnlinePlayers().forEach(p -> {
            double y = p.getLocation().getY();

            if(y >= gameManager.getCape()) { return; }

            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 30, 0, true,true,true));
        });
    }

    @EventHandler
    public void onTick(GameTickEvent e) {
        if(e.getTime() == (gameManager.getTimeForMeetup() + 360))  {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + "La capa estará subiendo 10 bloques en el próximo minuto!")),1L, 8400L);

        }

        if(e.getTime() == (gameManager.getTimeForMeetup() + 420))  {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> addCape(10),1L,8400);
        }
    }

    public void addCape(int capes) {
        int tid;

        tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()->gameManager.setCape(gameManager.getCape()+1), 120L, 120L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()->Bukkit.getScheduler().cancelTask(tid), 20L *capes*6);
    }
}
