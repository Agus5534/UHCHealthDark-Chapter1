package io.github.wickeddroidmx.plugin.utils.entities;

import org.bukkit.NamespacedKey;
import org.bukkit.entity.LivingEntity;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

public class EntityPersistentData {
    JavaPlugin plugin;
    NamespacedKey key;
    LivingEntity entity;

    public EntityPersistentData(JavaPlugin plugin, String identifier, LivingEntity entity) {
        this.plugin = plugin;
        this.entity = entity;

        key = new NamespacedKey(plugin,identifier);
    }

    public void setData(PersistentDataType persistentDataType, Object value) {
        entity.getPersistentDataContainer().set(key, persistentDataType, value);
    }

    public void removeData() {
        entity.getPersistentDataContainer().remove(key);
    }

    public boolean hasData(PersistentDataType persistentDataType) {
        return entity.getPersistentDataContainer().has(key, persistentDataType);
    }

    public Object getData(PersistentDataType persistentDataType) {
        return entity.getPersistentDataContainer().get(key,persistentDataType);
    }
}
