package io.github.wickeddroidmx.plugin.module.commands.parts;

import io.github.wickeddroidmx.plugin.poll.ConcursantTypes;
import kaptainwutax.biomeutils.biome.Biome;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BiomeCategoryPart implements ArgumentPart {

    private final String name;

    public BiomeCategoryPart(String name) { this.name = name; }

    @Override
    public List<?> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.parseType(argumentStack));
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        for(Biome.Category c : Biome.Category.values()) {
            suggestions.add(c.name());
        }

        return suggestions;
    }

    public String getName() {
        return this.name;
    }

    private Biome.Category parseType(ArgumentStack stack) {
        try {
            var cat = Biome.Category.valueOf(stack.next());
            return cat;
        } catch (Exception e) {
            throw new ArgumentParseException("That Biome Category not exist!");
        }
    }
}
