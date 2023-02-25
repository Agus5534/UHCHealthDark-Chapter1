package io.github.wickeddroidmx.plugin.utils.items;

import net.kyori.adventure.text.Component;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.attribute.AttributeModifier;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

@SuppressWarnings("unused")
public class ItemCreator extends ItemStack {
    public ItemCreator(final Material material) {
        super(material);
    }
    public ItemCreator(final ItemStack is) {
        super(is);
    }
    public ItemCreator amount(final int amount) {
        setAmount(amount);
        return this;
    }

    public ItemCreator amount(final int min, final int max) {
        setAmount(ThreadLocalRandom.current().nextInt(min,max));
        return this;
    }

    public ItemCreator name(final String name) {
        final ItemMeta meta = getItemMeta();
        meta.displayName(Component.text(name));
        setItemMeta(meta);
        return this;
    }

    public ItemCreator name(final Component name) {
        final ItemMeta meta = getItemMeta();
        meta.displayName(name);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator randomlyEnchanted(final double percentage) {
        double w = ThreadLocalRandom.current().nextDouble(1,100);
        if(percentage > w) {
            Enchantment enchantment = Arrays.stream(Enchantment.values())
                    .filter(enchant -> enchant.canEnchantItem(this))
                    .findAny().get();
            int level = new Random().nextInt(enchantment.getMaxLevel());
            if(level == 0) {
                level++;
            }
            addEnchantment(enchantment,level);
        }

        return this;
    }

    public ItemCreator randomlyEnchanted(final double percentage, final int maxEnchants) {
        double w = ThreadLocalRandom.current().nextDouble(1,100);
        int i = 0;
        while (i < maxEnchants) {
            if(percentage > w) {
                Enchantment enchantment = Arrays.stream(Enchantment.values())
                        .filter(enchant -> enchant.canEnchantItem(this))
                        .findAny().get();
                int level = new Random().nextInt(enchantment.getMaxLevel());
                if(level == 0) {
                    level++;
                }
                addEnchantment(enchantment,level);
            }
            i++;
        }
        return this;
    }

    public ItemCreator hasRandomEnchants() {
        double n = ThreadLocalRandom.current().nextInt(1, 50);
        double maxEnch = ThreadLocalRandom.current().nextInt(1, 3);
        int i = 0;

        while (i < maxEnch) {
            if(5 > n) {
                List<Enchantment> availableEnchants = new ArrayList<>();

                Arrays.stream(Enchantment.values())
                        .filter(e -> e.canEnchantItem(this))
                        .forEach(e -> availableEnchants.add(e));

                if(!availableEnchants.isEmpty()) {
                    var enchantment = availableEnchants.get(new Random().nextInt(availableEnchants.size()));

                    int level = new Random().nextInt(enchantment.getMaxLevel());

                    if(level == 0) { level++; }

                    if(enchantment.canEnchantItem(this)) {
                        addEnchantment(enchantment,level);
                    }
                }
            }
            i++;
        }

        return this;
    }

    public ItemCreator looksEnchanted() {
        final ItemMeta meta = getItemMeta();
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        meta.addEnchant(Enchantment.CHANNELING,1,false);

        setItemMeta(meta);
        return this;
    }

    public ItemCreator lore(final Component... txt) {
        final ItemMeta meta = getItemMeta();
        List<Component> lore = Arrays.asList(txt);
        if (lore == null) {
            lore = new ArrayList<>();
        }
        meta.lore(lore);
        setItemMeta(meta);
        return this;
    }

    @Deprecated
    public ItemCreator lore(final String... txt) {
        final ItemMeta meta = getItemMeta();
        List<String> lore = Arrays.asList(txt);

        if (lore == null) {
            lore = new ArrayList<>();
        }

        meta.setLore(lore);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator addItemFlag(ItemFlag flag) {
        final ItemMeta meta = getItemMeta();
        meta.addItemFlags(flag);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator removeItemFlag(ItemFlag flag) {
        final ItemMeta meta = getItemMeta();
        meta.removeItemFlags(flag);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator addAttribute(Attribute attribute, AttributeModifier attributeModifier) {
        final ItemMeta meta = getItemMeta();
        meta.addAttributeModifier(attribute,attributeModifier);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator removeAttribute(Attribute attribute) {
        final ItemMeta meta = getItemMeta();
        meta.removeAttributeModifier(attribute);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator setUnbreakable(boolean b) {
        final ItemMeta meta = getItemMeta();
        meta.setUnbreakable(b);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator potionEffect(PotionEffectType type, int duration, int amplifier) {
        PotionMeta potionMeta = (PotionMeta) getItemMeta();
        potionMeta.addCustomEffect(new PotionEffect(type, duration, amplifier),true);
        return this;
    }

    public ItemCreator enchants(final Enchantment enchantment, int level) {
        addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemCreator type(final Material material) {
        setType(material);
        return this;
    }

    public ItemCreator removeLore() {
        final ItemMeta meta = getItemMeta();
        meta.setLore(new ArrayList<>());
        setItemMeta(meta);
        return this;
    }

    public ItemCreator removeEnchantments() {
        for (final Enchantment e : getEnchantments().keySet()) {
            removeEnchantment(e);
        }
        return this;
    }

    public ItemCreator setCustomModelData(Integer id) {
        final ItemMeta meta = getItemMeta();
        meta.setCustomModelData(id);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator color(Color color) {
        if (getType() != Material.LEATHER_BOOTS && getType() != Material.LEATHER_CHESTPLATE
                && getType() != Material.LEATHER_HELMET && getType() != Material.LEATHER_LEGGINGS) {
            throw new IllegalArgumentException("Only for leather armor!");
        }
        LeatherArmorMeta meta = (LeatherArmorMeta) getItemMeta();
        meta.setColor(color);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator setSkullSkin(OfflinePlayer player) {
        final SkullMeta skullMeta = (SkullMeta) getItemMeta();
        skullMeta.setOwningPlayer(player);
        setItemMeta(skullMeta);
        return this;
    }

    public ItemCreator setPersistentData(JavaPlugin plugin, String identifier, PersistentDataType persistentDataType, Object value) {
        final ItemMeta meta = getItemMeta();
        ItemPersistentData data = new ItemPersistentData(plugin,identifier,meta);
        data.setData(persistentDataType,value);
        setItemMeta(meta);
        return this;
    }

    public ItemCreator removePersistentData(JavaPlugin plugin, String identifier) {
        final ItemMeta meta = getItemMeta();
        ItemPersistentData data = new ItemPersistentData(plugin,identifier,meta);
        data.removeData();
        setItemMeta(meta);
        return this;
    }

    public boolean hasEnchants() {
        final ItemMeta meta = getItemMeta();
        return meta.hasEnchants();
    }


}