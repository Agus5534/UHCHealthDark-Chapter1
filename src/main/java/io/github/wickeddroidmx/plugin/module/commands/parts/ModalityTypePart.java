package io.github.wickeddroidmx.plugin.module.commands.parts;

import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModalityTypePart implements ArgumentPart {

    private final String name;

    public ModalityTypePart(String name) { this.name = name; }

    private ModalityType parseType(ArgumentStack stack) {
        try {
            var modality = ModalityType.valueOf(stack.next());
            return modality;
        } catch (Exception e) {
            throw new ArgumentParseException("That RankType not exist!");
        }
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        for(var m : ModalityType.values()) {
            suggestions.add(m.name());
        }

        return suggestions;
    }

    @Override
    public List<?> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.parseType(argumentStack));
    }

    public String getName() {
        return this.name;
    }
}
