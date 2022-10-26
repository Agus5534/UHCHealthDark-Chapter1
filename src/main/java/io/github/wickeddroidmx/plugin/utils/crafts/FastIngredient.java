package io.github.wickeddroidmx.plugin.utils.crafts;

import org.bukkit.Material;

public class FastIngredient {
    private final Character character;
    private final Material material;

    public FastIngredient(Character character, Material material) {
        this.character = character;
        this.material = material;
    }

    public Character getCharacter() {
        return character;
    }

    public Material getMaterial() {
        return material;
    }
}
