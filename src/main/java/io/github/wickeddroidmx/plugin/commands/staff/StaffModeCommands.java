package io.github.wickeddroidmx.plugin.commands.staff;

import io.github.wickeddroidmx.plugin.menu.UhcStaffModesMenu;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
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
            names = "staffmodes"
    )
    public void staffModesCommand(@Sender Player sender, @Named("modalityType") ModalityType modalityType) {
        if(!sender.hasPermission("healthdark.host")) {
            sender.sendMessage(ChatUtils.formatComponentPrefix("&4Missing Permissions"));
            return;
        }

        if(modalityType == ModalityType.UHC) {
            sender.openInventory(staffMenu.getUHCInventory());
            return;
        }

        sender.openInventory(staffMenu.getModeInventory(modalityType));
    }
}
