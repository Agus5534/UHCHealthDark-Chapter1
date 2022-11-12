package io.github.wickeddroidmx.plugin.module.commands.parts;

import io.github.wickeddroidmx.plugin.commands.PlayerCommands;
import io.github.wickeddroidmx.plugin.poll.ConcursantTypes;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import me.fixeddev.commandflow.CommandContext;
import me.fixeddev.commandflow.exception.ArgumentParseException;
import me.fixeddev.commandflow.part.ArgumentPart;
import me.fixeddev.commandflow.part.CommandPart;
import me.fixeddev.commandflow.stack.ArgumentStack;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class UhcTeamPart implements ArgumentPart {
    private final String name;

    @Inject
    private TeamManager teamManager;

    public UhcTeamPart(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public List<String> getSuggestions(CommandContext commandContext, ArgumentStack stack) {
        String prefix = stack.hasNext() ? stack.next() : null;

        if(prefix == null) {
            return Collections.emptyList();
        }

        List<String> suggestions = new ArrayList<>();

        for(Player p : teamOwners()) {
            if(!suggestions.contains(p.getName())) {
                suggestions.add(p.getName());
            }
        }

        return suggestions;
    }

    @Override
    public List<?> parseValue(CommandContext commandContext, ArgumentStack argumentStack, CommandPart commandPart) throws ArgumentParseException {
        return Collections.singletonList(this.existsTeam(argumentStack));
    }

    private UhcTeam existsTeam(ArgumentStack stack) {
       try {
           Bukkit.getPlayer(stack.next());
       } catch (Exception e) {
           throw new ArgumentParseException("Given argument is not a valid player!");
       }

       var player = Bukkit.getPlayer(stack.next());
       if(teamOwners().contains(player)) {
           return teamManager.getTeam(player.getUniqueId());
       } else {
           throw new ArgumentParseException("That team doesn't exist!");
       }
    }

    private List<Player> teamOwners() {
        List<Player> owners = new ArrayList<>();

        teamManager.getUhcTeams().values().forEach(uT -> owners.add(uT.getOwner()));

        return owners;
    }

}
