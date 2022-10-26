package io.github.wickeddroidmx.plugin.utils.crafts;

import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class FastIngredient {
    private final Character character;
    private final ItemStack itemStack;

    public FastIngredient(Character character, Material material) {
        this.character = character;
        this.itemStack = new ItemCreator(material);
    }

    public FastIngredient(Character character, ItemStack itemStack) {
        this.character = character;
        this.itemStack = itemStack;
    }

    public Character getCharacter() {
        return character;
    }

    public ItemStack getItemStack() { return itemStack; }
}
