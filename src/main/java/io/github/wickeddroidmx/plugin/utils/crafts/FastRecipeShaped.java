package io.github.wickeddroidmx.plugin.utils.crafts;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;


public class FastRecipeShaped {
    JavaPlugin plugin;
    ItemStack resultItem;
    NamespacedKey key;
    ShapedRecipe recipe;
    public FastRecipeShaped(JavaPlugin plugin, ItemStack resultItem, String recipeName, String pattern) {
        this.plugin = plugin;
        this.resultItem = resultItem;
        key = new NamespacedKey(plugin,recipeName);
        recipe = new ShapedRecipe(key,resultItem);
        String[] patt = pattern.split(",");
        recipe.shape(patt[0],patt[1],patt[2]);
    }



    public FastRecipeShaped setItem(FastIngredient... fastIngredients) {
        for(FastIngredient fastIngredient : fastIngredients) {
            recipe.setIngredient(fastIngredient.getCharacter(),fastIngredient.getItemStack());
        }
        return this;
    }

    public FastRecipeShaped createRecipe() {
        Bukkit.addRecipe(recipe);

        return this;
    }
}

