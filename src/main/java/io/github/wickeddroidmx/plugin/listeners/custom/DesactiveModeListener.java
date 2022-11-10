package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.wickeddroidmx.plugin.events.game.DesactiveModeEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class DesactiveModeListener implements Listener {

    @EventHandler
    public void onDesactiveMode(DesactiveModeEvent e) {
        var modality = e.getModality();

        for (var player : Bukkit.getOnlinePlayers()) {
            switch (modality.getModalityType()) {
                case MODE -> player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("El modo %s &7se ha &cdesactivado &7correctamente.", modality.getName())));
                case UHC -> player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("Se ha desactivado correctamente %s.", modality.getName())));
                case SCENARIO -> player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("El scenario %s &7se ha &cdesactivado &7correctamente.", modality.getName())));
                case SETTING -> player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("La setting %s &7se ha &cdesactivado &7correctamente.", modality.getName())));
            }

            player.playSound(player.getLocation(), Sound.BLOCK_SOUL_SAND_BREAK, 1.0F, 1.0F);
        }
    }
}
