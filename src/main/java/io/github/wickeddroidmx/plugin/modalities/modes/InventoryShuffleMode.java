package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
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

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.*;
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

    @Inject
    private Main plugin;
    public InventoryShuffleMode() throws IllegalClassFormatException {
        super();
    }

    private void shuffleInventory(Player player) {
        List<ItemStack> invContents = new ArrayList<>();

        Arrays.stream(player.getInventory().getContents()).forEach(i -> invContents.add(i));

        for(int i = 0 ; i < ThreadLocalRandom.current().nextInt(4, 25) ; i++) {
            Collections.shuffle(invContents, new Random(ThreadLocalRandom.current().nextInt(Integer.MIN_VALUE, Integer.MAX_VALUE)));
        }

        player.getInventory().clear();

        invContents.forEach(i -> {
            if(i == null) {
                return;
            }

            if(i.getType() == Material.AIR) {
                return;
            }

            player.getInventory().addItem(i);
        });
    }

    @EventHandler
    public void onGameTick(GameTickEvent event) {
        if(event.getTime() % 900 == 0) {
            Bukkit.broadcast(ChatUtils.formatComponentPrefix("Randomizando inventarios."));

            Bukkit.getScheduler().runTaskLater(plugin, ()-> Bukkit.getOnlinePlayers().forEach(p -> shuffleInventory(p)), 5L);
        }
    }
}
