package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        name = "&cInventory Shuffle",
        material = Material.CHEST_MINECART,
        modalityType = ModalityType.MODE,
        key = "inv_shuffle",
        experimental = true,
        lore = "&7- Cada 15 minutos se mezcla tu inventario"
)
public class InventoryShuffleMode extends Modality {

    public InventoryShuffleMode() throws IllegalClassFormatException {
        super();
    }

    private void shuffleInventory(Player player) {
        List<ItemStack> invContents = new ArrayList<>();
        List<Integer> slotsNumbers = new ArrayList<>();

        for(int i = 0 ; i < 41 ; i++) {
            slotsNumbers.add(i);

            invContents.add(player.getInventory().getItem(i));
        }

        invContents.forEach(itemStack -> {
            int slot = slotsNumbers.get(ThreadLocalRandom.current().nextInt(slotsNumbers.size()));
            player.getInventory().setItem(slot, itemStack);

            slotsNumbers.remove(slot);
        });
    }

    @EventHandler
    public void onGameTick(GameTickEvent event) {
        if(event.getTime() % 900 == 0) {
            Bukkit.broadcast(ChatUtils.formatComponentPrefix("Randomizando inventarios."));

            Bukkit.getOnlinePlayers().forEach(p -> shuffleInventory(p));
        }
    }
}
