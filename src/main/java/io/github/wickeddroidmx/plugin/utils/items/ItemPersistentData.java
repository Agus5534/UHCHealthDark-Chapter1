package io.github.wickeddroidmx.plugin.utils.items;

import org.bukkit.NamespacedKey;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class ItemPersistentData {
    JavaPlugin plugin;
    NamespacedKey key;
    ItemMeta meta;

    public ItemPersistentData(JavaPlugin plugin, String identifier, ItemMeta meta) {
        this.plugin = plugin;
        key = new NamespacedKey(plugin, identifier);
        this.meta = meta;
    }

    public boolean hasData(PersistentDataType persistentDataType) {
        return meta.getPersistentDataContainer().has(key, persistentDataType);
    }

    public void setData(PersistentDataType persistentDataType, Object value) {
        meta.getPersistentDataContainer().set(key,persistentDataType,value);
    }

    public void removeData() {
        meta.getPersistentDataContainer().remove(key);
    }

    public Object getData(PersistentDataType persistentDataType) {
        return meta.getPersistentDataContainer().get(key,persistentDataType);
    }
}
