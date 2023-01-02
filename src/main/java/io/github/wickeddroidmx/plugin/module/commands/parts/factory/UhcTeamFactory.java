package io.github.wickeddroidmx.plugin.module.commands.parts.factory;

import io.github.wickeddroidmx.plugin.module.commands.parts.UhcTeamPart;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class UhcTeamFactory implements PartFactory {

    TeamManager teamManager;
    public UhcTeamFactory(TeamManager teamManager){
        this.teamManager = teamManager;
    }

    @Override
    public CommandPart createPart(String s, List<? extends Annotation> list) {
        return new UhcTeamPart(s, teamManager);
    }

}
