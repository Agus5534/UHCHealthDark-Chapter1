package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.inventory.ItemStack;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        key = "starterbook",
        name = "&bStarter Book",
        material = Material.BOOK,
        lore = {"&7- Todos los jugadores inician con un libro."}
)
public class StarterBookScenario extends Modality {

    public StarterBookScenario() throws IllegalClassFormatException {
        super();
    }


    @EventHandler
    public void onGameStart(GameStartEvent e) {
        for(Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().addItem(new ItemStack(Material.BOOK, 1));
        }
    }

    @EventHandler
    public void onPlayerLaterScatter(PlayerLaterScatterEvent e) {
        e.getPlayer().getInventory().addItem(new ItemStack(Material.BOOK, 1));
    }
}
