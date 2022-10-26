package io.github.wickeddroidmx.plugin.utils.crafts;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.plugin.java.JavaPlugin;

public class FastRecipeShapeless {
    JavaPlugin plugin;
    ItemStack result;
    NamespacedKey key;
    ShapelessRecipe recipe;
    public FastRecipeShapeless(JavaPlugin plugin, ItemStack result, String recipeName) {
        this.plugin = plugin;
        this.result = result;
        key = new NamespacedKey(plugin,recipeName);
        recipe = new ShapelessRecipe(key,result);
    }

    public FastRecipeShapeless setIngredients(ItemStack... ingredients) {
        for(ItemStack itemStack : ingredients) {
            recipe.addIngredient(itemStack);
        }
        return this;
    }

    public FastRecipeShapeless createRecipe() {
        Bukkit.addRecipe(recipe);

        return this;
    }
}
