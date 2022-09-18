package io.github.wickeddroidmx.plugin.modalities;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.ActiveModeEvent;
import io.github.wickeddroidmx.plugin.events.game.DesactiveModeEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

public abstract class Modality implements Listener  {

    @Inject
    private Main plugin;

    private final String name;
    private final String[] lore;
    private final Material material;
    private final ModalityType modalityType;
    private final String key;

    private boolean enable;

    public Modality(ModalityType modalityType, String key, String name, Material material, String... lore) {
        this.modalityType = modalityType;
        this.key = key;
        this.name = name;
        this.material = material;
        this.lore = lore;
    }

    public Modality(String... lore) throws IllegalClassFormatException {
        if(this.getClass().isAnnotationPresent(GameModality.class)) {
            var annotation = this.getClass().getAnnotation(GameModality.class);

            this.name = annotation.name();
            this.key = annotation.key();
            this.lore = lore;
            this.material = annotation.material();
            this.modalityType = annotation.modalityType();
        } else {
            throw new IllegalClassFormatException("Missing @GameModality argument or constructor");
        }
    }

    public void activeMode() {
        Bukkit.getPluginManager().registerEvents(this, plugin);

        Bukkit.getPluginManager().callEvent(new ActiveModeEvent(this));

        this.enable = true;
    }

    public void desactiveMode() {
        HandlerList.unregisterAll(this);

        Bukkit.getPluginManager().callEvent(new DesactiveModeEvent(this));
        this.enable = false;
    }

    public boolean isEnabled() {
        return enable;
    }

    public String getName() {
        return name;
    }

    public String getKey() {
        return key;
    }

    public ModalityType getModalityType() {
        return modalityType;
    }

    public ItemStack build() {
        return ItemBuilder.newBuilder(material)
                .setName(ChatUtils.format(name))
                .setLore(lore)
                .build();
    }
}
