package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class NoMobsScenario extends Modality {
    public NoMobsScenario() {
        super(ModalityType.SCENARIO, "no_mobs", "&cNo Mobs", Material.BONE,
                ChatUtils.format("&7- Los mobs no spawnean"));
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.DO_MOB_SPAWNING, false);
        });
    }
}

