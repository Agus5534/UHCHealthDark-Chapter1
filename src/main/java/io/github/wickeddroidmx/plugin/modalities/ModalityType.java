package io.github.wickeddroidmx.plugin.modalities;

import org.bukkit.Material;

public enum ModalityType {
    SCENARIO(Material.GOLD_NUGGET),
    TEAM(Material.RED_DYE),
    UHC(Material.ENCHANTED_GOLDEN_APPLE),
    MODE(Material.DIAMOND_PICKAXE),
    SETTING(Material.COMMAND_BLOCK);

    private Material material;

    ModalityType(Material material) {
        this.material = material;
    }

    public Material getMaterial() {
        return material;
    }
}
