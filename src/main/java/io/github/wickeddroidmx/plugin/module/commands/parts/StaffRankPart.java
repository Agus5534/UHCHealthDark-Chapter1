package io.github.wickeddroidmx.plugin.module.commands.parts;

import io.github.agus5534.hdbot.Ranks;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StaffRankPart implements ArgumentPart {

    private final String name;

    public StaffRankPart(String name) { this.name = name; }

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

        for(var r : Ranks.StaffRank.values()) {
            suggestions.add(r.name());
        }

        return suggestions;
    }



    public String getName() {
        return this.name;
    }

    public Ranks.StaffRank parseType(ArgumentStack stack) {
        try {
            var staffRank = Ranks.StaffRank.valueOf(stack.next());
            return staffRank;
        } catch (Exception e) {
            throw new ArgumentParseException("That StaffRank not exist!");
        }
    }
}
