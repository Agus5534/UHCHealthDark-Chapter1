package io.github.wickeddroidmx.plugin.module.commands.parts.factory;

import io.github.wickeddroidmx.plugin.module.commands.parts.DonatorRankPart;
import me.fixeddev.commandflow.annotated.part.PartFactory;
import me.fixeddev.commandflow.part.CommandPart;

import java.lang.annotation.Annotation;
import java.util.List;

public class DonatorRankFactory implements PartFactory {

    public DonatorRankFactory() {}

    @Override
    public CommandPart createPart(String s, List<? extends Annotation> list) {
        return new DonatorRankPart(s);
    }
}
