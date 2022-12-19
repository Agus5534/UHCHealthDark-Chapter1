package io.github.wickeddroidmx.plugin.commands;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.cache.ListCache;
import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.menu.UhcMenu;
import io.github.wickeddroidmx.plugin.menu.UhcModeMenu;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.UUID;

public class MainCommand implements CommandClass {

    private UhcModeMenu uhcModeMenu;

    @Inject
    private GameManager gameManager;
    @Inject
    private ExperimentManager experimentManager;
    @Inject
    private ModeManager modeManager;

    /*@Command(
            names = "uhc"
    )
    public void uhcCommand() {

    }*/

    @Command(
            names = "ironman"
    )
    public void ironMan(@Sender Player sender) {
        uhcModeMenu = new UhcModeMenu(modeManager, gameManager, experimentManager);
        UhcMenu uhcMenu = new UhcMenu(gameManager, experimentManager, uhcModeMenu, modeManager);

        sender.openInventory(uhcMenu.getIronManMenu());

        sender.sendMessage(ChatUtils.formatComponentNotification("Este comando será reemplazado en un futuro por &6/uhc"));
    }

    @Command(
            names = "modality"
    )
    public void modalityCommand(@Sender Player sender) {
        sender.openInventory(uhcModeMenu.getSelectInventory());

        sender.sendMessage(ChatUtils.formatComponentNotification("Este comando será reemplazado en un futuro por &6/uhc"));
    }

    @Command(
            names = "uhc"
    )
    public void uhcCommand(@Sender Player sender) {
        uhcModeMenu = new UhcModeMenu(modeManager, gameManager, experimentManager);
        UhcMenu uhcMenu = new UhcMenu(gameManager, experimentManager, uhcModeMenu, modeManager);

        sender.openInventory(uhcMenu.getConfigMenu());
    }

}
