package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.GameRule;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

public class NoAchievementsMode extends Modality {
    public NoAchievementsMode() {
        super(ModalityType.MODE, "no_achievements", "&aNo Logros", Material.NETHER_STAR,
                ChatUtils.format("&7- Los logros no se mostrarán."));
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Bukkit.getWorlds().forEach(world -> {
            world.setGameRule(GameRule.ANNOUNCE_ADVANCEMENTS, false);
        });
    }
}
