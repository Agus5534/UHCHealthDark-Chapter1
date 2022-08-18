package io.github.wickeddroidmx.plugin.modalities.uhc;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.worldborder.WorldBorderSetEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
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
import java.util.Random;

public class UhcRunMode extends Modality {

    @Inject
    private GameManager gameManager;

    public UhcRunMode() {
        super(ModalityType.UHC, "uhc_run", "&bUhc Run", Material.ENCHANTED_GOLDEN_APPLE,
                ChatUtils.format("&7- El apple rate esta subido"),
                ChatUtils.format("&7- Hay comandos nuevos, como /fullbright"),
                ChatUtils.format("&7- No hay limite de scenarios."));
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

        if (new Random().nextInt(100) >= 95) {
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
            if (random.nextInt(100) >= 90) {
                block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE));
            }
        }
    }

    @Override
    public void activeMode() {
        super.activeMode();

        gameManager.setTimeForPvP(1800);
        gameManager.setTimeForMeetup(3600);

        gameManager.setScenarioLimit(false);
        gameManager.setRunMode(true);
    }

    @Override
    public void desactiveMode() {
        super.desactiveMode();

        gameManager.setTimeForPvP(3600);
        gameManager.setTimeForMeetup(7200);
        gameManager.setRunMode(false);
        gameManager.setScenarioLimit(true);
    }
}
