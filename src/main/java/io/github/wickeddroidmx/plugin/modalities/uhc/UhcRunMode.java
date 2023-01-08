package io.github.wickeddroidmx.plugin.modalities.uhc;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderSetEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.player.DeathType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        modalityType = ModalityType.UHC,
        key = "uhc_run",
        name = "&bUhc Run",
        material = Material.DIAMOND_BOOTS,
        lore = {
                "&7- El apple rate está alterado",
                "&7- Hay comandos nuevos",
                "&7- No hay límite de Scenarios."
        }
)
public class UhcRunMode extends Modality {

    @Inject
    private GameManager gameManager;

    public UhcRunMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        var gameManager = e.getGameManager();

        gameManager.setRunMode(true);

        Bukkit.getPluginManager().callEvent(new WorldBorderSetEvent(1000));
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent e) {
        var block = e.getBlock();

        if (ThreadLocalRandom.current().nextInt(1,100) <= gameManager.getAppleRate()) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE));
        }
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        Block block = event.getBlock();

        if (block.getType() == Material.GRINDSTONE) {
            event.getPlayer().sendMessage(ChatUtils.PREFIX + "No puedes usar grindstones en uhc run.");
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var block = e.getBlock();
        var random = new Random();


        if (block.getType().toString().toLowerCase().endsWith("leaves")) {
            if (ThreadLocalRandom.current().nextInt(1,100) <= gameManager.getAppleRate()) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }

    @Override
    public void activeMode() {
        super.activeMode();

        if(!gameManager.isSkyHighMode()) {
            gameManager.setTimeForPvP(1800);
            gameManager.setTimeForMeetup(3600);
            gameManager.setCobwebLimit(8);
        }

        gameManager.setScenarioLimit(false);
        gameManager.setRunMode(true);
        gameManager.setDeathType(DeathType.INSTANT_SPECTATE);
    }

    @Override
    public void desactiveMode() {
        super.desactiveMode();

        gameManager.setTimeForPvP(gameManager.isSkyHighMode() ? 3540 : 3600);
        gameManager.setTimeForMeetup(gameManager.isSkyHighMode() ? 3600 : 7200);
        gameManager.setCobwebLimit(gameManager.isSkyHighMode() ? 1 : 12);
        gameManager.setRunMode(false);
        gameManager.setScenarioLimit(true);
        gameManager.setDeathType(DeathType.NORMAL);
    }
}
