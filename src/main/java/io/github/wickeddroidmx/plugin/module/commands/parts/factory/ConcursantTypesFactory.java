package io.github.wickeddroidmx.plugin.module.commands.parts.factory;

import io.github.wickeddroidmx.plugin.module.commands.parts.ConcursantTypesPart;
import io.github.wickeddroidmx.plugin.module.commands.parts.TeamFlagsPart;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class ConcursantTypesFactory implements PartFactory {

    public ConcursantTypesFactory() {}

    @Override
    public CommandPart createPart(String s, List<? extends Annotation> list) {
        return new ConcursantTypesPart(s);
    }
}
