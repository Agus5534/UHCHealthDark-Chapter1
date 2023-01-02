package io.github.wickeddroidmx.plugin.modalities.modes;

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
        modalityType = ModalityType.MODE,
        key = "no_achievements",
        name = "&aNo Logros",
        material = Material.EXPERIENCE_BOTTLE,
        lore = {"&7- Los logros estarán ocultos al chat."}
)
public class NoAchievementsMode extends Modality {
    public NoAchievementsMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        });
    }
}
