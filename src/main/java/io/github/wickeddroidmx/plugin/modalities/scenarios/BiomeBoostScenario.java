package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&bBiome Boost",
        key = "biome_boost",
        material = Material.RED_SAND,
        modalityType = ModalityType.SCENARIO,
        experimental = false,
        lore = {
                "&7- Los biomas dan efectos al estar en ellos"
        }
)
public class BiomeBoostScenario extends Modality {

    public BiomeBoostScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameTick(GameTickEvent event) {
        if(event.getTime() % 2 == 0) {
            Bukkit.getOnlinePlayers().forEach(this::checkBiome);
        }
    }

    private void checkBiome(Player player) {
        var loc = player.getLocation();

        var biome = loc.getBlock().getBiome();

        switch (biome) {
            case DESERT, DESERT_HILLS, DESERT_LAKES, BADLANDS, BADLANDS_PLATEAU, ERODED_BADLANDS, MODIFIED_BADLANDS_PLATEAU, MODIFIED_WOODED_BADLANDS_PLATEAU, WOODED_BADLANDS_PLATEAU, SAVANNA, SAVANNA_PLATEAU, SHATTERED_SAVANNA, SHATTERED_SAVANNA_PLATEAU, BASALT_DELTAS ->
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FIRE_RESISTANCE, 100, 0, true, false, true));
            case PLAINS, SUNFLOWER_PLAINS, SOUL_SAND_VALLEY -> player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 100, 0, true, false, true));
            case MOUNTAINS, MOUNTAIN_EDGE, GRAVELLY_MOUNTAINS, MODIFIED_GRAVELLY_MOUNTAINS, TAIGA_MOUNTAINS, SNOWY_MOUNTAINS, SNOWY_TAIGA_MOUNTAINS, WOODED_MOUNTAINS, NETHER_WASTES ->
                    player.addPotionEffect(new PotionEffect(PotionEffectType.FAST_DIGGING, 100, 0, true, false, true));
            case RIVER, OCEAN, COLD_OCEAN, DEEP_COLD_OCEAN, DEEP_OCEAN, WARM_OCEAN, LUKEWARM_OCEAN, FROZEN_OCEAN, DEEP_WARM_OCEAN, DEEP_FROZEN_OCEAN, DEEP_LUKEWARM_OCEAN, SWAMP, SWAMP_HILLS ->
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DOLPHINS_GRACE, 100, 0, true, false, true));
            case FOREST, FLOWER_FOREST, BIRCH_FOREST, WARPED_FOREST,DARK_FOREST, TALL_BIRCH_FOREST, BIRCH_FOREST_HILLS, DARK_FOREST_HILLS, CRIMSON_FOREST ->
                    player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 100, 0, true, false, true));
            case THE_END, END_BARRENS, END_HIGHLANDS, END_MIDLANDS, SMALL_END_ISLANDS, THE_VOID ->
                    player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW_FALLING, 100, 0, true, false, true));
        }
    }
}
