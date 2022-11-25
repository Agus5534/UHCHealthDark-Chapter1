package io.github.wickeddroidmx.plugin.commands;

import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import io.github.wickeddroidmx.plugin.menu.UhcMenu;
import io.github.wickeddroidmx.plugin.menu.UhcModeMenu;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

public class MainCommand implements CommandClass {

    @Inject
    private UhcModeMenu uhcModeMenu;

    @Inject
    private UhcMenu uhcMenu;

    @Inject
    private ExperimentManager experimentManager;

    /*@Command(
            names = "uhc"
    )
    public void uhcCommand() {

    }*/

    @Command(
            names = "ironman"
    )
    public void ironMan(@Sender Player sender) {
        if(!experimentManager.hasExperiment(sender, "IRONMAN_COMMAND_EXPERIMENT")) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("No estás autorizado a utilizar esto."));
            return;
        }

        sender.openInventory(uhcMenu.getIronManMenu());
    }

    @Command(
            names = "modality"
    )
    public void modalityCommand(@Sender Player sender) {
        sender.openInventory(uhcModeMenu.getSelectInventory());
    }

}
