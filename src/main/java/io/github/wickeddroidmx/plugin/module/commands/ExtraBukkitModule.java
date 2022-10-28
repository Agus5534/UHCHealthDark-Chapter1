package io.github.wickeddroidmx.plugin.module.commands;

import io.github.wickeddroidmx.plugin.module.commands.parts.factory.BiomeFactory;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import org.bukkit.block.Biome;

public class ExtraBukkitModule extends AbstractModule {

    public ExtraBukkitModule(){
    }

    public void configure() {
        this.bindFactory(Biome.class, new BiomeFactory());
    }

}
