package io.github.wickeddroidmx.plugin.modalities;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.ActiveModeEvent;
import io.github.wickeddroidmx.plugin.events.game.DesactiveModeEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.items.ItemCreator;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import team.unnamed.gui.core.item.type.ItemBuilder;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Arrays;

public abstract class Modality implements Listener  {

    @Inject
    private Main plugin;

    private final String name;
    private final String[] lore;
    private final Material material;
    private final ModalityType modalityType;
    private final String key;
    private final boolean experimental;
    private boolean enable;

    public Modality(ModalityType modalityType, String key, String name, Material material, String... lore) {
        this.modalityType = modalityType;
        this.key = key;
        this.name = name;
        this.material = material;
        this.lore = lore;
        this.experimental = false;
    }

    @Deprecated
    public Modality(String... lore) throws IllegalClassFormatException {
        if(this.getClass().isAnnotationPresent(GameModality.class)) {
            var annotation = this.getClass().getAnnotation(GameModality.class);

            this.name = annotation.name();
            this.key = annotation.key();
            this.lore = lore;
            this.material = annotation.material();
            this.modalityType = annotation.modalityType();
            this.experimental = annotation.experimental();
        } else {
            throw new IllegalClassFormatException("Missing @GameModality annotation or constructor");
        }
    }
    public Modality() throws IllegalClassFormatException {
        if(this.getClass().isAnnotationPresent(GameModality.class)) {
            var annotation = this.getClass().getAnnotation(GameModality.class);
            String[] s = annotation.lore();
            Arrays.stream(s).forEach(st -> ChatUtils.format(st));

            this.name = annotation.name();
            this.key = annotation.key();
            this.lore = s;
            this.material = annotation.material();
            this.modalityType = annotation.modalityType();
            this.experimental = annotation.experimental();
        } else {
            throw new IllegalClassFormatException("Missing @GameModality annotation or constructor");
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

    public boolean isExperimental() {
        return experimental;
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
        var item = new ItemCreator(material)
                .name(ChatUtils.formatC(name))
                .lore(lore)
                .addItemFlag(ItemFlag.HIDE_ENCHANTS);

        if(this.isEnabled()) { item.looksEnchanted(); }

        return item;
    }
}
