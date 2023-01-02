package io.github.wickeddroidmx.plugin.module.commands.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.bukkit.block.Biome;

import java.util.*;

public class BiomePart implements ArgumentPart {
    private final String name;

    public BiomePart(String name) {
        this.name = name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        Iterator iterator = Arrays.stream(Biome.values()).iterator();

        while (iterator.hasNext()) {
            suggestions.add(iterator.next().toString());
        }

        if (suggestions.size() == 1) {
            return Collections.emptyList();
        } else {
            return suggestions;
        }
    }

    @Override
    public List<Biome> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.checkBiome(argumentStack));
    }

    public String getName() {
        return this.name;
    }

    private Biome checkBiome(ArgumentStack stack) {
        try {
            var biome = Biome.valueOf(stack.next());
            return biome;
        } catch (Exception e) {
            throw new ArgumentParseException("That Biome not exist!");
        }
    }

}
