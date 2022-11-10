package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.wickeddroidmx.plugin.events.game.ActiveModeEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class ActiveModeListener implements Listener {

    @EventHandler
    public void onActiveMode(ActiveModeEvent e) {
        var modality = e.getModality();

        for (var player : Bukkit.getOnlinePlayers()) {
            switch (modality.getModalityType()) {
                case MODE -> player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("El modo %s &7se ha &aactivado &7correctamente.", modality.getName())));
                case UHC -> player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("Se ha cambiado correctamente a %s.", modality.getName())));
                case SCENARIO -> player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("El scenario %s &7se ha &aactivado &7correctamente.", modality.getName())));
                case SETTING -> player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("La setting %s &7se ha &aactivado &7correctamente.", modality.getName())));
            }

            if(modality.isExperimental()) {
                player.sendMessage(ChatUtils.formatC(ChatUtils.PREFIX + "La modalidad fue marcada como &cexperimental &7y puede contener &cerrores&7, no se recomienda su uso."));
            }

            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
        }
    }
}
