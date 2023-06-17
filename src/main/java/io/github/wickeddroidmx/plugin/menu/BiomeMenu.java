package io.github.wickeddroidmx.plugin.menu;

import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import io.github.wickeddroidmx.plugin.utils.world.WorldGenerator;
import kaptainwutax.biomeutils.biome.Biomes;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.inventory.Inventory;
import team.unnamed.gui.abstraction.item.ItemClickable;
import team.unnamed.gui.core.gui.type.GUIBuilder;

import java.util.*;
import java.util.stream.Collectors;

public class BiomeMenu {
    private Map<Biome, kaptainwutax.biomeutils.biome.Biome.Category> biomeMaterialMap;
    private Map<kaptainwutax.biomeutils.biome.Biome.Category, Material> categoryMaterialMap;

    public BiomeMenu() {
        if(biomeMaterialMap == null) {
            biomeMaterialMap = new HashMap<>();
        }

        if(categoryMaterialMap == null) {
            categoryMaterialMap = new HashMap<>();
        }

        for(var b : Biome.values()) {
            configureBiomeMap(b);
        }

        configureCategoryMap();
    }

    public Inventory bannedBiomesMenu(WorldGenerator generator) {
        var bannedBiomes = generator.getUhcWorld().getBannedBiomes();
        return GUIBuilder.builderPaginated(Biome.class, ChatUtils.format("&6Biomas Baneados"))
                .fillBorders(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name("")).build())
                .setEntities(Arrays.stream(Biome.values()).sorted(Comparator.comparing(bannedBiomes::contains)).collect(Collectors.toList()))
                .setItemIfNotEntities(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name("")).build())
                .setItemParser(biome -> ItemClickable.builderCancellingEvent().setItemStack(
                        new ItemCreator(categoryMaterialMap.get(biomeMaterialMap.get(biome))).name(ChatUtils.formatC( "&6" + biome.name().replaceAll("_", " "))).lore(ChatUtils.formatC((bannedBiomes.contains(biome) ? "&4Baneado" : "&aPermitido")))
                ).setAction(e -> {

                    if(generator.getUhcWorld().isBannedBiome(biome)) {
                        generator.getUhcWorld().removeBannedBiomes(biome);
                    } else {
                        generator.getUhcWorld().addBannedBiomes(biome);
                    }

                    var iC = new ItemCreator(e.getCurrentItem()).lore(ChatUtils.formatC(generator.getUhcWorld().getBannedBiomes().contains(biome) ? "&4Baneado" : "&aPermitido"));

                    e.setCurrentItem(iC);
                    return true;
                }).build())
                .setNextPageItem(item -> ItemClickable.builder(53).setItemStack(new ItemCreator(Material.ARROW).name(ChatUtils.formatC("&6Siguiente página - " + item))).build())
                .setPreviousPageItem(item -> ItemClickable.builder(45).setItemStack(new ItemCreator(Material.ARROW).name(ChatUtils.formatC("&6Anterior página - " + item))).build())
                .setBounds(10, 44)
                .setItemsPerRow(7)
                .build();
    }

    public Inventory bannedCategoriesMenu(WorldGenerator generator) {
        var bannedCategories = generator.getUhcWorld().getBannedCategories();
        return GUIBuilder.builderPaginated(kaptainwutax.biomeutils.biome.Biome.Category.class, ChatUtils.format("&6Biomas Baneados"))
                .fillBorders(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.BLACK_STAINED_GLASS_PANE).name("")).build())
                .setEntities(Arrays.stream(kaptainwutax.biomeutils.biome.Biome.Category.values()).sorted(Comparator.comparing(bannedCategories::contains)).collect(Collectors.toList()))
                .setItemIfNotEntities(ItemClickable.builderCancellingEvent().setItemStack(new ItemCreator(Material.LIGHT_GRAY_STAINED_GLASS_PANE).name("")).build())
                .setItemParser(category -> ItemClickable.builderCancellingEvent().setItemStack(
                        new ItemCreator(categoryMaterialMap.get(categoryMaterialMap.get(category))).name(ChatUtils.formatC( "&6" + category.name().replaceAll("_", " "))).lore(ChatUtils.formatC((bannedCategories.contains(category) ? "&4Baneada" : "&aPermitida")))
                ).setAction(e -> {

                    if(generator.getUhcWorld().isBannedCategory(category)) {
                        generator.getUhcWorld().removeBannedCategory(category);
                    } else {
                        generator.getUhcWorld().addBannedCategory(category);
                    }

                    var iC = new ItemCreator(e.getCurrentItem()).lore(ChatUtils.formatC(generator.getUhcWorld().getBannedBiomes().contains(category) ? "&4Baneada" : "&aPermitida"));

                    e.setCurrentItem(iC);
                    return true;
                }).build())
                .setNextPageItem(item -> ItemClickable.builder(53).setItemStack(new ItemCreator(Material.ARROW).name(ChatUtils.formatC("&6Siguiente página - " + item))).build())
                .setPreviousPageItem(item -> ItemClickable.builder(45).setItemStack(new ItemCreator(Material.ARROW).name(ChatUtils.formatC("&6Anterior página - " + item))).build())
                .setBounds(10, 44)
                .setItemsPerRow(7)
                .build();
    }

    private void configureBiomeMap(Biome biome) {
        kaptainwutax.biomeutils.biome.Biome b = null;
        try {
            var a = Biomes.class.getDeclaredField(biome.name());
            b = (kaptainwutax.biomeutils.biome.Biome) a.get(kaptainwutax.biomeutils.biome.Biome.class);
        } catch (Exception e) {
            biomeMaterialMap.put(biome, kaptainwutax.biomeutils.biome.Biome.Category.NONE);
            return;
        }

        biomeMaterialMap.put(biome, b.getCategory());
    }

    private void configureCategoryMap() {
        biomeMaterialMap.values().forEach(c -> categoryMaterialMap.put(c, this.getMaterialFromCategory(c)));
    }

    private Material getMaterialFromCategory(kaptainwutax.biomeutils.biome.Biome.Category category) {
        switch (category) {
            case OCEAN -> {
                return Material.PACKED_ICE;
            }
            case ICY -> {
                return Material.SNOW_BLOCK;
            }
            case MESA, BADLANDS_PLATEAU -> {
                return Material.RED_SAND;
            }
            case BEACH, DESERT -> {
                return Material.SAND;
            }
            case RIVER, SWAMP -> {
                return Material.CLAY;
            }
            case TAIGA -> {
                return Material.SPRUCE_LOG;
            }
            case FOREST -> {
                return Material.OAK_LOG;
            }
            case JUNGLE -> {
                return Material.JUNGLE_LOG;
            }
            case NETHER -> {
                return Material.NETHERRACK;
            }
            case PLAINS -> {
                return Material.GRASS_BLOCK;
            }
            case SAVANNA -> {
                return Material.ACACIA_LOG;
            }
            case THE_END -> {
                return Material.END_STONE;
            }
            case MUSHROOM -> {
                return Material.RED_MUSHROOM_BLOCK;
            }
            case EXTREME_HILLS, NONE -> {
                return Material.STONE;
            }
            default -> {
                return Material.STRUCTURE_VOID;
            }
        }
    }
}
