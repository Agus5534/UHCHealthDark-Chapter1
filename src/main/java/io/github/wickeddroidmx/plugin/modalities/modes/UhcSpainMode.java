package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;

public class UhcSpainMode extends Modality {

    @Inject
    private Main plugin;

    public UhcSpainMode() {
        super(ModalityType.MODE, "uhc_spain", "&6Uhc España", Material.GOLDEN_APPLE,
                ChatUtils.format("&7- Se podrán hacer las Hyper Golden Apples"),
                ChatUtils.format("&7- Y las Super Golden Apples"));
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        addRecipe();
    }

    @EventHandler
    public void onPlayerConsume(PlayerItemConsumeEvent e) {
        var player = e.getPlayer();
        var attribute = player.getAttribute(Attribute.GENERIC_MAX_HEALTH);
        var item = e.getItem();

        if (item.hasItemMeta() && item.getItemMeta().hasDisplayName()) {
            if (item.getItemMeta().getDisplayName().equals(ChatUtils.format("&6Hyper Golden Apple"))) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.removePotionEffect(PotionEffectType.ABSORPTION);
                    player.removePotionEffect(PotionEffectType.REGENERATION);
                }, 1L);

                if (attribute != null) {
                    attribute.setBaseValue(attribute.getBaseValue() + 4.0D);

                    player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 0));
                }
            } else if (item.getItemMeta().getDisplayName().equals(ChatUtils.format("&6Super Golden Apple"))) {
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    player.removePotionEffect(PotionEffectType.ABSORPTION);
                    player.removePotionEffect(PotionEffectType.REGENERATION);
                }, 1L);

                player.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20, 1));
            }
        }
    }

    public void addRecipe() {
        var shapedRecipe = new ShapedRecipe(new NamespacedKey(plugin, "super_golden_apple"), ItemBuilder.newBuilder(Material.GOLDEN_APPLE).setName(ChatUtils.format("&6Super Golden Apple")).build());

        shapedRecipe.shape(
                "XXX",
                "XAX",
                "XXX"
        );
        shapedRecipe.setIngredient('X', new RecipeChoice.MaterialChoice(Material.GOLD_INGOT));
        shapedRecipe.setIngredient('A', new RecipeChoice.MaterialChoice(Material.GOLDEN_APPLE));

        var hyperRecipe = new ShapedRecipe(new NamespacedKey(plugin, "hyper_golden_apple"), ItemBuilder.newBuilder(Material.GOLDEN_APPLE).setName(ChatUtils.format("&6Hyper Golden Apple")).build());

        hyperRecipe.shape(
                "XXX",
                "XAX",
                "XXX"
        );
        hyperRecipe.setIngredient('X', new RecipeChoice.MaterialChoice(Material.GOLD_INGOT));
        hyperRecipe.setIngredient('A', new RecipeChoice.ExactChoice(ItemBuilder.newBuilder(Material.GOLDEN_APPLE).setName(ChatUtils.format("&6Super Golden Apple")).build()));

        Bukkit.addRecipe(shapedRecipe);
        Bukkit.addRecipe(hyperRecipe);
    }
}
