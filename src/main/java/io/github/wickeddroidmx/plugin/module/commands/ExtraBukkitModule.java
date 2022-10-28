package io.github.wickeddroidmx.plugin.module.commands;

import io.github.wickeddroidmx.plugin.module.commands.parts.ChatColorPart;
import io.github.wickeddroidmx.plugin.module.commands.parts.factory.BiomeFactory;
import io.github.wickeddroidmx.plugin.module.commands.parts.factory.ChatColorFactory;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;

public class ExtraBukkitModule extends AbstractModule {

    public ExtraBukkitModule(){
    }

    public void configure() {
        this.bindFactory(Biome.class, new BiomeFactory());
        this.bindFactory(ChatColor.class, new ChatColorFactory());
    }

}
