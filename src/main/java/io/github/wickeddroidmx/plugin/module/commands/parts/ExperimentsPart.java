package io.github.wickeddroidmx.plugin.module.commands.parts;

import io.github.wickeddroidmx.plugin.experiments.Experiment;
import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ExperimentsPart implements ArgumentPart {

    private final String name;
    private final ExperimentManager experimentManager;

    public ExperimentsPart(String name, ExperimentManager experimentManager) {
        this.name = name;
        this.experimentManager = experimentManager;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        for(var s : experimentManager.getStringExperimentHashMap().keySet()) {
            suggestions.add(s);
        }

        return suggestions;
    }

    @Override
    public List<Experiment> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.checkExperiment(argumentStack));
    }

    public String getName() {
        return this.name;
    }

    private Experiment checkExperiment(ArgumentStack stack) {
        var exp = experimentManager.getExperimentByKey(stack.next());

        if(exp == null) {
            throw new ArgumentParseException("No se encuentra ese experimento!");
        }

        return exp;
    }
}
