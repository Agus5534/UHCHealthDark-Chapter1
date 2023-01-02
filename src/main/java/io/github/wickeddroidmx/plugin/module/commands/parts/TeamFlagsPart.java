package io.github.wickeddroidmx.plugin.module.commands.parts;

import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TeamFlagsPart implements ArgumentPart {
    private final String name;

    public TeamFlagsPart(String name) { this.name = name; }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        for(var f : TeamFlags.values()) {
            suggestions.add(f.getName());
        }

        if (suggestions.size() == 1) {
            return Collections.emptyList();
        } else {
            return suggestions;
        }
    }

    @Override
    public List<?> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.checkFlag(argumentStack));
    }

    public String getName() {
        return this.name;
    }

    private TeamFlags checkFlag(ArgumentStack stack) {
        var flag = getFlagByName(stack.next());

        if(flag == null) { throw new ArgumentParseException("That Flag not exist!"); }

        return flag;
    }

    private TeamFlags getFlagByName(String s) {
        TeamFlags f = null;
        for(TeamFlags teamFlags : TeamFlags.values()) {
            if (teamFlags.getName().equalsIgnoreCase(s)) {
                f = teamFlags;
            }
        }
        return f;
    }
}
