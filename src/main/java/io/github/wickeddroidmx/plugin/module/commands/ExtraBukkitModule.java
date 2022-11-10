package io.github.wickeddroidmx.plugin.module.commands;

import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.module.commands.parts.ChatColorPart;
import io.github.wickeddroidmx.plugin.module.commands.parts.ConcursantTypesPart;
import io.github.wickeddroidmx.plugin.module.commands.parts.factory.*;
import io.github.wickeddroidmx.plugin.poll.ConcursantTypes;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;

public class ExtraBukkitModule extends AbstractModule {

    public ExtraBukkitModule(){
    }

    public void configure() {
        this.bindFactory(Biome.class, new BiomeFactory());
        this.bindFactory(ChatColor.class, new ChatColorFactory());
        this.bindFactory(TeamFlags.class, new TeamFlagsFactory());
        this.bindFactory(ConcursantTypes.class, new ConcursantTypesFactory());
        this.bindFactory(ModalityType.class, new ModalityTypeFactory());
    }

}
