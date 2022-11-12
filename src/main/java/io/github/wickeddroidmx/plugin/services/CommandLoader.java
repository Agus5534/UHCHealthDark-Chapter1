package io.github.wickeddroidmx.plugin.services;

import io.github.wickeddroidmx.plugin.commands.MainCommand;
import io.github.wickeddroidmx.plugin.commands.PlayerCommands;
import io.github.wickeddroidmx.plugin.commands.staff.*;
import io.github.wickeddroidmx.plugin.commands.teams.TeamCommands;
import io.github.wickeddroidmx.plugin.module.commands.ExtraBukkitModule;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import me.fixeddev.commandflow.annotated.AnnotatedCommandTreeBuilder;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.part.PartInjector;
import me.fixeddev.commandflow.annotated.part.defaults.DefaultsModule;
import me.fixeddev.commandflow.bukkit.BukkitCommandManager;
import me.fixeddev.commandflow.bukkit.factory.BukkitModule;
import me.yushust.inject.InjectAll;

@InjectAll
public class CommandLoader implements Loader {

    // Main Commands
    private MainCommand mainCommand;
    private PlayerCommands playerCommands;
    private TeamCommands teamCommands;

    // Staff Commands
    private StaffCommands staffCommands;
    private StaffGameCommands staffGameCommands;
    private StaffTeamCommands staffTeamCommands;
    private StaffModeCommands staffModeCommands;
    private StaffMeetupCommands staffMeetupCommands;
    private StaffWorldCommands staffWorldCommands;
    private TeamManager teamManager;

    @Override
    public void load() {
        registerCommands(
                mainCommand,
                staffCommands,
                teamCommands,
                playerCommands,
                staffGameCommands,
                staffTeamCommands,
                staffModeCommands,
                staffMeetupCommands,
                staffWorldCommands
        );
    }

    private void registerCommands(CommandClass... commandClasses) {
        PartInjector partInjector = PartInjector.create();
        partInjector.install(new DefaultsModule());
        partInjector.install(new BukkitModule());
        partInjector.install(new ExtraBukkitModule(teamManager));

        BukkitCommandManager commandManager = new BukkitCommandManager("template");
        AnnotatedCommandTreeBuilder treeBuilder = AnnotatedCommandTreeBuilder.create(partInjector);

        for (CommandClass commandClass : commandClasses) {
            commandManager.registerCommands(treeBuilder.fromClass(commandClass));
        }
    }
}
