package io.github.wickeddroidmx.plugin.module.commands.parts;

import io.github.agus5534.hdbot.Ranks;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DonatorRankPart implements ArgumentPart {


    private final String name;

    public DonatorRankPart(String name) { this.name = name; }

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

        for(var r : Ranks.DonatorRank.values()) {
            suggestions.add(r.name());
        }

        return suggestions;
    }



    public String getName() {
        return this.name;
    }

    public Ranks.DonatorRank parseType(ArgumentStack stack) {
        try {
            var donatorRank = Ranks.DonatorRank.valueOf(stack.next());
            return donatorRank;
        } catch (Exception e) {
            throw new ArgumentParseException("That DonatorRank not exist!");
        }
    }
}
