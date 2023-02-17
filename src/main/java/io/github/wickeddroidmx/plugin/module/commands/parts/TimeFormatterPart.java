package io.github.wickeddroidmx.plugin.module.commands.parts;

import io.github.wickeddroidmx.plugin.poll.ConcursantTypes;
import io.github.wickeddroidmx.plugin.utils.time.TimeFormatter;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TimeFormatterPart implements ArgumentPart {

    private final String name;

    public TimeFormatterPart(String name) {
        this.name = name;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        var l = Arrays.asList("h", "m", "s", "t");

        for(var s : l) {
            if(stack.current() != null & validNumber(stack.current())) {
                suggestions.add(stack.current() + s);
            } else if(stack.current().equals("")){
                suggestions.add(s);
            }
        }

        return suggestions;
    }

    @Override
    public List<?> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.parseTime(argumentStack));
    }

    private TimeFormatter parseTime(ArgumentStack stack) {
        try {
            return new TimeFormatter(stack.next());
        } catch (Exception e) {
            throw new ArgumentParseException("That provided time is not valid!");
        }
    }

    private boolean validNumber(String s) {
        try {
            Long.parseLong(s);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
