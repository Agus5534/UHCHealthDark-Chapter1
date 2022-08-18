package io.github.wickeddroidmx.plugin.services;

import io.github.wickeddroidmx.plugin.commands.MainCommand;
import io.github.wickeddroidmx.plugin.commands.PlayerCommands;
import io.github.wickeddroidmx.plugin.commands.StatsCommand;
import io.github.wickeddroidmx.plugin.commands.staff.StaffCommands;
import io.github.wickeddroidmx.plugin.commands.staff.StaffGameCommands;
import io.github.wickeddroidmx.plugin.commands.staff.StaffModeCommands;
import io.github.wickeddroidmx.plugin.commands.staff.StaffTeamCommands;
import io.github.wickeddroidmx.plugin.commands.teams.TeamCommands;
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
    private StatsCommand statsCommand;

    // Staff Commands
    private StaffCommands staffCommands;
    private StaffGameCommands staffGameCommands;
    private StaffTeamCommands staffTeamCommands;
    private StaffModeCommands staffModeCommands;

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
                statsCommand
        );
    }

    private void registerCommands(CommandClass... commandClasses) {
        PartInjector partInjector = PartInjector.create();
        partInjector.install(new DefaultsModule());
        partInjector.install(new BukkitModule());

        BukkitCommandManager commandManager = new BukkitCommandManager("template");
        AnnotatedCommandTreeBuilder treeBuilder = AnnotatedCommandTreeBuilder.create(partInjector);

        for (CommandClass commandClass : commandClasses) {
            commandManager.registerCommands(treeBuilder.fromClass(commandClass));
        }
    }
}
