package io.github.wickeddroidmx.plugin.modalities.uhc;

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
        modalityType = ModalityType.UHC,
        key = "skyhigh",
        name = "&bUHC SkyHigh",
        material = Material.ELYTRA,
        lore = {"&7- PvP en capa +150"}
)
public class UhcSkyHighMode extends Modality {
    @Inject
    private PlayerManager playerManager;
    @Inject
    private Main plugin;
    @Inject
    private GameManager gameManager;

    int taskID;

    public UhcSkyHighMode() throws IllegalClassFormatException {
        super();
    }


    @Override
    public void activeMode() {
        super.activeMode();
        gameManager.setSkyHighMode(true);
        gameManager.setTimeForPvP(3540);
        gameManager.setTimeForMeetup(3600);
        gameManager.setCobwebLimit(1);

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> check(), 2L, 2L);
    }

    @Override
    public void desactiveMode() {
        super.desactiveMode();
        gameManager.setSkyHighMode(false);
        gameManager.setTimeForPvP(gameManager.isRunMode() ? 1800 : 3600);
        gameManager.setTimeForPvP(gameManager.isRunMode() ? 3600 : 7200);
        gameManager.setCobwebLimit(gameManager.isRunMode() ? 8 : 12);
        //gameManager.setScenarioLimit(true);

        Bukkit.getScheduler().cancelTask(taskID);
    }

    private void check() {
        if(gameManager.getGameState() != GameState.MEETUP) {
            return;
        }

        if(!gameManager.isSkyHighMode()) { return; }

        Bukkit.getOnlinePlayers().forEach(p -> {
            var uhcPlayer = playerManager.getPlayer(p.getUniqueId());

            if(uhcPlayer == null) { return; }

            if(!uhcPlayer.isAlive()) { return; }

            double y = p.getLocation().getY();

            if(y >= gameManager.getCape()) { return; }

            int amplifier = (int) ((gameManager.getCape() - y) / 50);

            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 45, amplifier, true,true,true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 45, amplifier, true,true,true));
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

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()->Bukkit.getScheduler().cancelTask(tid), 20*capes*6);
    }
}
