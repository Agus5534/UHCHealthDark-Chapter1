package io.github.wickeddroidmx.plugin.modalities.settings;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        name = "&6Spawner Last Breath",
        modalityType = ModalityType.SETTING,
        material = Material.SPAWNER,
        experimental = true,
        key = "spawner_lastbreath",
        lore = {
                "&7- Los spawners al ser destruidos spawnean una vez más"
        }
)
public class SpawnerLastBreathSetting extends Modality {

    @Inject
    private Main plugin;

    public SpawnerLastBreathSetting() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if(event.getBlock().getType() != Material.SPAWNER) { return; }

        var spawner = (CreatureSpawner)event.getBlock().getState();
        var item = event.getPlayer().getInventory().getItemInMainHand();

        spawner.resetTimer();
        spawner.setSpawnCount(ThreadLocalRandom.current().nextInt(1, 4));
        spawner.setMinSpawnDelay(1);
        spawner.setMaxSpawnDelay(4);
        spawner.setDelay(-1);

        event.setCancelled(true);

        Bukkit.getScheduler().runTaskLater(plugin, ()->event.getBlock().breakNaturally(item), 11L);
    }
}
