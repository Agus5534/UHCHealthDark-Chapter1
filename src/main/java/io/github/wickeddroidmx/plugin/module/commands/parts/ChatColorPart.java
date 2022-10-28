package io.github.wickeddroidmx.plugin.module.commands.parts;

import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.bukkit.ChatColor;

import java.util.*;

public class ChatColorPart implements ArgumentPart {
    private final String name;

    public ChatColorPart(String name) { this.name = name; }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        for(ChatColor c : ChatColor.values()) {
            suggestions.add(c.name());
        }

        if (suggestions.size() == 1) {
            return Collections.emptyList();
        } else {
            return suggestions;
        }
    }

    @Override
    public List<?> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.checkColor(argumentStack));
    }

    public String getName() {
        return this.name;
    }

    private ChatColor checkColor(ArgumentStack stack) {
        try {
            var color = ChatColor.valueOf(stack.next());
            return color;
        } catch (Exception e) {
            throw new ArgumentParseException("That Color not exist!");
        }
    }
}
