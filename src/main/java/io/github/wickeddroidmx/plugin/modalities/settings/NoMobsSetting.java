package io.github.wickeddroidmx.plugin.modalities.settings;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.SETTING,
        key = "no_mobs",
        name = "&cNo Mobs",
        material = Material.ZOMBIE_SPAWN_EGG,
        lore = {"&7- Los mobs no spawnean."}
)
public class NoMobsSetting extends Modality {
    public NoMobsSetting() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        });
    }
}

