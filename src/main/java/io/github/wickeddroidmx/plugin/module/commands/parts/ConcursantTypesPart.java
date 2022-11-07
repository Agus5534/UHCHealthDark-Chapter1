package io.github.wickeddroidmx.plugin.module.commands.parts;

import io.github.wickeddroidmx.plugin.poll.ConcursantTypes;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ConcursantTypesPart implements ArgumentPart {

    private final String name;

    public ConcursantTypesPart(String name) { this.name = name; }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        for(ConcursantTypes c : ConcursantTypes.values()) {
            suggestions.add(c.name());
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

    private ConcursantTypes parseType(ArgumentStack stack) {
        try {
            var concursant = ConcursantTypes.valueOf(stack.next());
            return concursant;
        } catch (Exception e) {
            throw new ArgumentParseException("That ConcursantType not exist!");
        }
    }
}
