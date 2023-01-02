package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.events.game.GameCutCleanEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

import java.lang.instrument.IllegalClassFormatException;
import java.util.Random;

@GameModality(
        modalityType = ModalityType.SCENARIO,
        key = "cut_clean",
        name = "&bCut Clean",
        material = Material.IRON_PICKAXE,
        lore = {
                "&7- Todos los minerales se cocinarán.",
                "&7- Los animales soltarán su carne cocida"
        }
)
public class CutCleanScenario extends Modality {
    public CutCleanScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e) {
        var player = e.getPlayer();

        var block = e.getBlock();
        var blockType = block.getType();

        var item = player.getInventory().getItemInMainHand();
        var itemType = item.getType();

        if (blockType == Material.GRAVEL) {
            e.setDropItems(false);

            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.FLINT));
        }

        if (!isTool(itemType) || item.containsEnchantment(Enchantment.SILK_TOUCH))
            return;

        if(block.getDrops(item).isEmpty()) {
            return;
        }

        if (blockType == Material.COPPER_ORE
                || blockType == Material.DEEPSLATE_COPPER_ORE
                || blockType == Material.IRON_ORE
                || blockType == Material.GOLD_ORE
                || blockType == Material.ANCIENT_DEBRIS
                || blockType == Material.DEEPSLATE_IRON_ORE
                || blockType == Material.DEEPSLATE_GOLD_ORE) {
            e.setDropItems(false);

            switch (blockType) {
                case COPPER_ORE, DEEPSLATE_COPPER_ORE -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.COPPER_INGOT));

                    spawnXP(block.getLocation(), 2);
                }
                case IRON_ORE, DEEPSLATE_IRON_ORE -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));

                    spawnXP(block.getLocation(), 2);
                }
                case GOLD_ORE, DEEPSLATE_GOLD_ORE -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));

                    spawnXP(block.getLocation(), 2);
                }
                case ANCIENT_DEBRIS -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.NETHERITE_SCRAP));

                    spawnXP(block.getLocation(), 4);
                }
            }
        }
    }

    @EventHandler
    public void onEntityDeath(EntityDeathEvent e) {
        var entity = e.getEntity();

        var random = new Random();
        var amount = random.nextInt(3)+1;

        var location = entity.getLocation();

        if (entity instanceof Pig
                || entity instanceof Cow
                || entity instanceof Chicken
                || entity instanceof Sheep) {
            e.getDrops().clear();

            if (entity instanceof Pig) {
                e.getDrops().add(new ItemStack(Material.COOKED_PORKCHOP, amount));
            } else if (entity instanceof Cow) {
                e.getDrops().add(new ItemStack(Material.COOKED_BEEF, amount));
                e.getDrops().add(new ItemStack(Material.LEATHER, random.nextInt(2)+1));
            } else if (entity instanceof Sheep) {
                e.getDrops().add(new ItemStack(Material.COOKED_MUTTON, amount));
            }else {
                e.getDrops().add(new ItemStack(Material.COOKED_CHICKEN, amount));
                e.getDrops().add(new ItemStack(Material.FEATHER, random.nextInt(1)+1));
            }
        }
    }

    @EventHandler
    public void onCutClean(GameCutCleanEvent e) {
        var player = e.getPlayer();

        var block = e.getBlock();
        var blockType = block.getType();

        var item = player.getInventory().getItemInMainHand();
        var itemType = item.getType();

        if (blockType == Material.GRAVEL) {
            e.setDropItems(false);

            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.FLINT));
        }

        if (!isTool(itemType) || item.containsEnchantment(Enchantment.SILK_TOUCH))
            return;

        if(block.getDrops(item).isEmpty()) {
            return;
        }

        if(blockType == Material.DIAMOND_ORE || blockType == Material.DEEPSLATE_DIAMOND_ORE) {
            block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND));

            spawnXP(block.getLocation(), 4);
        }

        if (blockType == Material.COPPER_ORE
                || blockType == Material.DEEPSLATE_COPPER_ORE
                || blockType == Material.IRON_ORE
                || blockType == Material.GOLD_ORE
                || blockType == Material.ANCIENT_DEBRIS
                || blockType == Material.DEEPSLATE_IRON_ORE
                || blockType == Material.DEEPSLATE_GOLD_ORE
                || blockType == Material.DIAMOND_ORE
                || blockType == Material.DEEPSLATE_DIAMOND_ORE) {
            e.setDropItems(false);

            switch (blockType) {
                case COPPER_ORE, DEEPSLATE_COPPER_ORE -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.COPPER_INGOT));

                    spawnXP(block.getLocation(), 2);
                }
                case IRON_ORE, DEEPSLATE_IRON_ORE -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.IRON_INGOT));

                    spawnXP(block.getLocation(), 2);
                }
                case GOLD_ORE, DEEPSLATE_GOLD_ORE -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.GOLD_INGOT));

                    spawnXP(block.getLocation(), 2);
                }
                case ANCIENT_DEBRIS -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.NETHERITE_SCRAP));

                    spawnXP(block.getLocation(), 4);
                }
                case DIAMOND_ORE, DEEPSLATE_DIAMOND_ORE -> {
                    block.getWorld().dropItemNaturally(block.getLocation(), new ItemStack(Material.DIAMOND));

                    spawnXP(block.getLocation(), 4);
                }
            }
        }
    }

    private void spawnXP(Location location, int xp) {
        var experienceOrb = (ExperienceOrb) location.getWorld().spawnEntity(location, EntityType.EXPERIENCE_ORB);

        experienceOrb.setExperience(xp);
    }

    private boolean isTool(Material material) {
        return material.toString().toLowerCase().endsWith("pickaxe")
                || material.toString().toLowerCase().endsWith("hoe")
                || material.toString().toLowerCase().endsWith("axe")
                || material.toString().toLowerCase().endsWith("shovel");
    }
}
