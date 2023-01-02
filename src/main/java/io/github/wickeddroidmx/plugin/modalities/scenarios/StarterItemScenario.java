package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.player.PlayerLaterScatterEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        name = "&6Starter Item",
        material = Material.COMMAND_BLOCK,
        key = "starter_item",
        experimental = true,
        lore = "&7- Inicias con un item aleatorio",
        modalityType = ModalityType.SCENARIO
)
public class StarterItemScenario extends Modality {

    private Material[] blockedMaterials;
    private List<Material> possibleDrops;

    public StarterItemScenario() throws IllegalClassFormatException {
        super();
    }

    @Override
    public void activeMode() {
        super.activeMode();

        blockedMaterials = new Material[] {
                Material.AIR,
                Material.CAVE_AIR,
                Material.VOID_AIR,
                Material.END_CRYSTAL,
                Material.END_GATEWAY,
                Material.DEBUG_STICK,
                Material.MOVING_PISTON,
                Material.DEAD_TUBE_CORAL,
                Material.DEAD_TUBE_CORAL_FAN,
                Material.DEAD_TUBE_CORAL_WALL_FAN,
                Material.WHITE_WALL_BANNER,
                Material.ATTACHED_MELON_STEM,
                Material.ATTACHED_PUMPKIN_STEM,
                Material.BEETROOTS,
                Material.LAVA_CAULDRON,
                Material.LAVA,
                Material.WATER,
                Material.WATER_CAULDRON,
                Material.POWDER_SNOW_CAULDRON,
                Material.TRIPWIRE,
                Material.POTATOES,
                Material.BAMBOO_SAPLING,
                Material.COCOA,
                Material.PISTON_HEAD,
                Material.REDSTONE_WIRE,
                Material.FROSTED_ICE,
                Material.CAVE_VINES,
                Material.FIRE,
                Material.BUBBLE_COLUMN,
                Material.SOUL_FIRE,
                Material.TALL_SEAGRASS,
                Material.NETHER_PORTAL,
                Material.END_PORTAL,
                Material.CARROTS,
                Material.POWDER_SNOW,
                Material.COMMAND_BLOCK_MINECART,
                Material.COMMAND_BLOCK,
                Material.CHAIN_COMMAND_BLOCK,
                Material.REPEATING_COMMAND_BLOCK,
                Material.BARRIER,
                Material.STRUCTURE_BLOCK,
                Material.STRUCTURE_VOID,
                Material.JIGSAW,
        };

        possibleDrops = new ArrayList<>();

        for(Material m : Material.values()) {
            if(!m.isLegacy() && !m.isAir() && !Arrays.asList(blockedMaterials).contains(m) && !m.getKey().getKey().contains("wall") && !m.getKey().getKey().contains("potted") && !m.getKey().getKey().contains("candle_cake") && !m.getKey().getKey().contains("plant") && !m.getKey().getKey().contains("stem") && !m.getKey().getKey().contains("bush")) {
                possibleDrops.add(m);
            }
        }
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        Bukkit.getOnlinePlayers().forEach(p -> {
            int slot = ThreadLocalRandom.current().nextInt(0, 40);

            p.getInventory().setItem(slot, new ItemCreator(possibleDrops.get(ThreadLocalRandom.current().nextInt(possibleDrops.size()))));
        });
    }

    @EventHandler
    public void onLaterScatter(PlayerLaterScatterEvent event) {
        var p = event.getPlayer();
        int slot = ThreadLocalRandom.current().nextInt(0, 40);

        p.getInventory().setItem(slot, new ItemCreator(possibleDrops.get(ThreadLocalRandom.current().nextInt(possibleDrops.size()))));
    }
}
