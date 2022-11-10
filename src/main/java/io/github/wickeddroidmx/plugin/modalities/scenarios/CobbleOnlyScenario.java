package io.github.wickeddroidmx.plugin.modalities.scenarios;


import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerScatteredEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&8Cobble Only",
        material = Material.COBBLESTONE,
        modalityType = ModalityType.SCENARIO,
        key = "cobble_only",
        lore = {"Toda piedra dropea cobblestone."}
)
public class CobbleOnlyScenario extends Modality {

    @Inject
    private PlayerManager playerManager;

    public CobbleOnlyScenario() throws IllegalClassFormatException {
        super();
    }

    @Override
    public void desactiveMode() {
        super.desactiveMode();

        playerManager.getUhcPlayers().values().forEach(uP -> uP.setCobbleOnly(false));
    }

    @EventHandler
    public void onScatter(GameStartEvent event) {
        Bukkit.getOnlinePlayers().forEach(p -> p.chat("/cobbleonly"));
    }

    @EventHandler
    public void onLaterScatter(PlayerLaterScatterEvent event) {
        event.getPlayer().chat("/cobbleonly");
    }
}
