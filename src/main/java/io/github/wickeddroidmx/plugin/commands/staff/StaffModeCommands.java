package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.menu.UhcStaffModesMenu;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

@Command(
        names = "staffmodes",
        permission = "healthdark.staff"
)
public class StaffModeCommands implements CommandClass {

    @Inject
    private UhcStaffModesMenu staffMenu;

    @Command(
            names = "modes"
    )
    public void modesCommand(@Sender Player sender) {
        sender.openInventory(staffMenu.getModeInventory(ModalityType.MODE));
    }

    @Command(
            names = "scenarios"
    )
    public void scenarioCommand(@Sender Player sender) {
        sender.openInventory(staffMenu.getModeInventory(ModalityType.SCENARIO));
    }

    @Command(
            names = "uhc"
    )
    public void uhcCommand(@Sender Player sender) {
         sender.openInventory(staffMenu.getUHCInventory());
    }

    @Command(
            names = "team"
    )
    public void teamCommand(@Sender Player sender) {
        sender.openInventory(staffMenu.getModeInventory(ModalityType.TEAM));
    }
}
