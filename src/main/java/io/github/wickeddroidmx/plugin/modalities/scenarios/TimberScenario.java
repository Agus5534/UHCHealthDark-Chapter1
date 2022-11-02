package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.LeavesDecayEvent;
import org.bukkit.inventory.ItemStack;

import javax.inject.Inject;
import java.util.concurrent.ThreadLocalRandom;

public class TimberScenario extends Modality {

    @Inject
    private Main plugin;

    @Inject
    private GameManager gameManager;

    public TimberScenario() {
        super(ModalityType.SCENARIO, "timber", "&aTimber", Material.OAK_LOG,
                ChatUtils.format("&7- Los arboles se talan de una."));
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var block = e.getBlock();

        if (isLog(block.getType())) {
            breakTree(block, block.getLocation());
        }
    }

    private void breakTree(Block block, Location centralLoc) {
        if (isLog(block.getType())) {
            block.breakNaturally(new ItemStack(Material.AIR), true);
            double radius = radius(block.getType());

            for (var face : BlockFace.values()){
                if(isLog(block.getRelative(face).getType())) {
                    Bukkit.getScheduler().runTaskLater(plugin, () -> breakTree(block.getRelative(face), centralLoc), 3L);
                    continue;
                }

                if(!isLog(block.getRelative(BlockFace.UP).getType())) {
                    Bukkit.getScheduler().runTaskLater(plugin, ()-> breakLeaves(block.getRelative(face), radius, block.getLocation()), 3L);
                }
            }
        }
    }

    private void breakLeaves(Block block, double radius, Location centralLoc) {
        if(isLeaves(block.getType())) {
            if(centralLoc.distance(block.getLocation()) > radius) {
                return;
            }

            block.breakNaturally(new ItemStack(Material.AIR), true);

            if(block.getType().toString().toLowerCase().endsWith("_leaves")) {
                if(gameManager.isRunMode()) {
                    if (ThreadLocalRandom.current().nextInt(1,100) <= gameManager.getAppleRate()) {
                        block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.APPLE));
                    }
                }
            }

            for(var face : BlockFace.values()) {
                Bukkit.getScheduler().runTaskLater(plugin, ()-> breakLeaves(block.getRelative(face), radius, centralLoc),3L);
            }
        }
    }

    private boolean isLog(Material material) {
        return (material.toString().toLowerCase().endsWith("_log") || material.toString().toLowerCase().endsWith("_stem"));
    }

    private boolean isLeaves(Material material) {
        return (material.toString().toLowerCase().endsWith("_leaves") || material == Material.BROWN_MUSHROOM_BLOCK || material == Material.RED_MUSHROOM_BLOCK || material.toString().toLowerCase().endsWith("_log"));
    }

    private double radius(Material material) {
        switch (material) {
            case JUNGLE_LOG -> {
                return 10.0D;
            }
            case DARK_OAK_LOG, MUSHROOM_STEM, SPRUCE_LOG -> {
                return 6.0D;
            }
            default -> {
                return 5.0D;
            }
        }
    }
}
