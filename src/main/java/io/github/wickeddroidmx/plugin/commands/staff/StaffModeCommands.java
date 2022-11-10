package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.menu.UhcStaffModesMenu;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import me.fixeddev.commandflow.annotated.CommandClass;
import me.fixeddev.commandflow.annotated.annotation.Command;
import me.fixeddev.commandflow.annotated.annotation.Named;
import me.fixeddev.commandflow.bukkit.annotation.Sender;
import org.bukkit.entity.Player;

import javax.inject.Inject;

public class StaffModeCommands implements CommandClass {

    @Inject
    private UhcStaffModesMenu staffMenu;

    @Command(
            names = "staffmodes",
            permission = "healthdark.staff"
    )
    public void staffModesCommand(@Sender Player sender, @Named("modalityType") ModalityType modalityType) {
        if(modalityType == ModalityType.UHC) {
            sender.openInventory(staffMenu.getUHCInventory());
            return;
        }

        sender.openInventory(staffMenu.getModeInventory(modalityType));
    }
}
