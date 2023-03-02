package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.agus5534.hdbot.Ranks;
import io.github.agus5534.hdbot.minecraft.events.PlayerAssignRankEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PlayerAssignRankListener implements Listener {

    @EventHandler
    public void onAssignRank(PlayerAssignRankEvent event) {
        if(!event.getPlayer().isOp()) { return; }

        var ranks = event.getStaffRanks().values();

        if(!ranks.contains(Ranks.StaffRank.DEVELOPER) &&
        !ranks.contains(Ranks.StaffRank.OWNER) &&
        !ranks.contains(Ranks.StaffRank.HEAD_ADMIN) &&
        !ranks.contains(Ranks.StaffRank.ADMIN) &&
        !ranks.contains(Ranks.StaffRank.OPERATOR)) {
            event.getPlayer().setOp(false);

            event.getPlayer().sendMessage(ChatUtils.formatComponentNotification(
                    "Ya ha llegado el nuevo sistema de permisos al plugin, se te ha revocado el permiso de operador y puede llegar a existir alguna incompatibilidad de permisos. Ante cualquier problema, reportarlo."
            ));
        }
    }
}
