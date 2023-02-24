package io.github.wickeddroidmx.plugin.module.commands;

import io.github.agus5534.hdbot.Ranks;
import io.github.wickeddroidmx.plugin.experiments.Experiment;
import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.module.commands.parts.TimeFormatterPart;
import io.github.wickeddroidmx.plugin.module.commands.parts.factory.*;
import io.github.wickeddroidmx.plugin.poll.ConcursantTypes;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.time.TimeFormatter;
import me.fixeddev.commandflow.annotated.part.AbstractModule;
import org.bukkit.ChatColor;
import org.bukkit.block.Biome;


public class ExtraBukkitModule extends AbstractModule {


    private TeamManager teamManager;
    private ExperimentManager experimentManager;

    public ExtraBukkitModule(TeamManager teamManager, ExperimentManager experimentManager) {
        this.teamManager = teamManager;
        this.experimentManager = experimentManager;
    }

    public void configure() {
        this.bindFactory(Biome.class, new BiomeFactory());
        this.bindFactory(ChatColor.class, new ChatColorFactory());
        this.bindFactory(TeamFlags.class, new TeamFlagsFactory());
        this.bindFactory(ConcursantTypes.class, new ConcursantTypesFactory());
        this.bindFactory(ModalityType.class, new ModalityTypeFactory());
        this.bindFactory(UhcTeam.class, new UhcTeamFactory(teamManager));
        this.bindFactory(Experiment.class, new ExperimentsFactory(experimentManager));
        this.bindFactory(Ranks.StaffRank.class, new StaffRankFactory());
        this.bindFactory(Ranks.DonatorRank.class, new DonatorRankFactory());
        this.bindFactory(kaptainwutax.biomeutils.biome.Biome.Category.class, new BiomeCategoryFactory());
        this.bindFactory(TimeFormatter.class, new TimeFormatterFactory());
    }

}
