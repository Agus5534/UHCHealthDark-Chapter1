package io.github.wickeddroidmx.plugin.commands;

import io.github.wickeddroidmx.plugin.menu.UhcStatsMenu;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.OptArg;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.checkerframework.checker.units.qual.C;

import javax.inject.Inject;

@Command(
        names = "stats"
)
public class StatsCommand implements CommandClass {

    @Inject
    private UhcStatsMenu statsMenu;

    @Command(
            names = "get"
    )
    public void getCommand(@Sender Player sender, @OptArg OfflinePlayer player) {
        sender.openInventory(statsMenu.getStatsInventory(player));
    }
}
