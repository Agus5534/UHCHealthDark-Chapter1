package io.github.wickeddroidmx.plugin.services;

import io.github.wickeddroidmx.plugin.commands.StaffCommands;
import io.github.wickeddroidmx.plugin.commands.host.HostCommand;
import io.github.wickeddroidmx.plugin.commands.MainCommand;
import io.github.wickeddroidmx.plugin.commands.PlayerCommands;
import io.github.wickeddroidmx.plugin.commands.PrefixesCommands;
import io.github.wickeddroidmx.plugin.commands.teams.TeamCommands;
import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
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
    // Commands
    private HostCommand hostCommand;
    private StaffCommands staffCommands;
    private TeamManager teamManager;
    private ExperimentManager experimentManager;
    private PrefixesCommands prefixesCommands;
    private MainCommand mainCommand;
    private PlayerCommands playerCommands;
    private TeamCommands teamCommands;

    @Override
    public void load() {
        registerCommands(
                mainCommand,
                staffCommands,
                teamCommands,
                playerCommands,
                hostCommand,
                prefixesCommands
        );
    }

    private void registerCommands(CommandClass... commandClasses) {
        PartInjector partInjector = PartInjector.create();
        partInjector.install(new DefaultsModule());
        partInjector.install(new BukkitModule());
        partInjector.install(new ExtraBukkitModule(teamManager, experimentManager));

        BukkitCommandManager commandManager = new BukkitCommandManager("template");
        AnnotatedCommandTreeBuilder treeBuilder = AnnotatedCommandTreeBuilder.create(partInjector);

        for (CommandClass commandClass : commandClasses) {
            commandManager.registerCommands(treeBuilder.fromClass(commandClass));
        }
    }
}
