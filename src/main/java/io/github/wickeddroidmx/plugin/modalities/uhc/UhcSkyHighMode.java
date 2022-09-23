package io.github.wickeddroidmx.plugin.modalities.uhc;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderMoveEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
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

public class UhcSkyHighMode extends Modality {
    @Inject
    private PlayerManager playerManager;
    @Inject
    private Main plugin;
    @Inject
    private GameManager gameManager;

    int cape = 150;

    int taskID;

    public UhcSkyHighMode() {
        super(ModalityType.UHC, "skyhigh", "&bUHC SkyHigh", Material.ELYTRA,
                ChatUtils.format("&7- PVP en capa +150"));
    }


    @Override
    public void activeMode() {
        super.activeMode();
        gameManager.setRunMode(false);
        gameManager.setSkyHighMode(true);
        gameManager.setScenarioLimit(true);
        gameManager.setTimeForPvP(3540);
        gameManager.setTimeForMeetup(3600);

        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> check(), 2L, 2L);
    }

    @Override
    public void desactiveMode() {
        super.desactiveMode();
        gameManager.setSkyHighMode(false);
        gameManager.setTimeForPvP(3600);
        gameManager.setTimeForMeetup(7200);
        gameManager.setScenarioLimit(true);

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

            if(y >= cape) { return; }

            int amplifier = (int) ((cape - y) / 25);

            p.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 60, amplifier, true,true,true));
            p.addPotionEffect(new PotionEffect(PotionEffectType.WEAKNESS, 60, amplifier, true,true,true));
        });
    }

    @EventHandler
    public void onTick(GameTickEvent e) {
        if(e.getTime() % (60 * 67) == 0) {
            Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()->addCape(15),1L,8400);
        }
    }

    public void addCape(int capes) {
        int tid;

        tid = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()->cape++, 1L, 40L);

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, ()->Bukkit.getScheduler().cancelTask(tid), 20*capes*2);
    }
}
