package io.github.wickeddroidmx.plugin.module.commands.parts.factory;

import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import io.github.wickeddroidmx.plugin.module.commands.parts.ExperimentsPart;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ExperimentsFactory implements PartFactory {

    private final ExperimentManager experimentManager;

    public ExperimentsFactory(ExperimentManager experimentManager) {
        this.experimentManager = experimentManager;
    }

    @Override
    public CommandPart createPart(String s, List<? extends Annotation> list) {
        return new ExperimentsPart(s, experimentManager);
    }
}
