package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import javax.inject.Inject;
import java.util.ArrayList;

public class GraveRobbersScenario extends Modality {

    @Inject
    private Main plugin;

    public GraveRobbersScenario() {
        super(ModalityType.SCENARIO, "grave_robbers", "&cGrave Robbers", Material.MOSSY_COBBLESTONE,
                ChatUtils.format("&7- Al matar a alguien tendrá un tumba."));
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        var location = player.getLocation();
        var newDrops = new ArrayList<>(e.getDrops());

        e.getDrops().clear();

        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            var chest = createDoubleChestAt(location);

            for (var drop : newDrops) {
                if (drop != null && drop.getType() != Material.AIR) {
                    chest.getInventory().addItem(drop);
                }

                createHolo(new Location(location.getWorld(), location.getBlockX() + 0.5D, location.getBlockY() + 0.5D, location.getBlockZ() + 1), ChatUtils.format("&6Tumba de " + player.getName()));
            }
        }, 1L);
    }

    @EventHandler
    public void onRightClickHologram(PlayerInteractAtEntityEvent e) {
        var entity = e.getRightClicked();
        if (entity instanceof ArmorStand) {
            var block = entity.getLocation().getBlock();
            if (block.getType() == Material.CHEST) {
                var chest = (Chest) block.getState();
                chest.open();
                e.getPlayer().openInventory(chest.getInventory());
            }

        }
    }

    public Chest createDoubleChestAt(Location loc) {
        Block leftSide = loc.getBlock();
        Block rightSide = loc.clone().add(0, 0, -1).getBlock();
        rightSide.getRelative(BlockFace.UP).setType(Material.AIR);
        leftSide.getRelative(BlockFace.UP).setType(Material.AIR);

        leftSide.setType(Material.CHEST);
        rightSide.setType(Material.CHEST);

        BlockData leftData = leftSide.getBlockData();
        ((Directional) leftData).setFacing(BlockFace.EAST);
        leftSide.setBlockData(leftData);

        org.bukkit.block.data.type.Chest chestDataLeft = (org.bukkit.block.data.type.Chest) leftData;
        chestDataLeft.setType(org.bukkit.block.data.type.Chest.Type.RIGHT);
        leftSide.setBlockData(chestDataLeft);

        BlockData rightData = rightSide.getBlockData();
        ((Directional) rightData).setFacing(BlockFace.EAST);
        rightSide.setBlockData(rightData);

        org.bukkit.block.data.type.Chest chestDataRight = (org.bukkit.block.data.type.Chest) rightData;
        chestDataRight.setType(org.bukkit.block.data.type.Chest.Type.LEFT);
        rightSide.setBlockData(chestDataRight);
        return (Chest) leftSide.getState();
    }

    private ArmorStand createHolo(Location location, String name) {
        ArmorStand armorStand = location.getWorld().spawn(location, ArmorStand.class);
        armorStand.setCollidable(false);
        armorStand.setVisible(false);
        armorStand.setAI(false);
        armorStand.setArms(false);
        armorStand.setCanMove(false);
        armorStand.setGravity(false);
        armorStand.setSmall(true);
        armorStand.setCustomNameVisible(true);
        armorStand.setCustomName(name);

        return armorStand;
    }
}
