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

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public Modality() throws IllegalClassFormatException {
        if(this.getClass().isAnnotationPresent(GameModality.class)) {
            var annotation = this.getClass().getAnnotation(GameModality.class);
            List<String> s = new ArrayList<>();
            Arrays.stream(annotation.lore()).forEach(str -> s.add(ChatUtils.format(str)));

            this.name = annotation.name();
            this.key = annotation.key();
            this.lore = s.toArray(new String[0]);
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

    public void deactivateMode() {
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
