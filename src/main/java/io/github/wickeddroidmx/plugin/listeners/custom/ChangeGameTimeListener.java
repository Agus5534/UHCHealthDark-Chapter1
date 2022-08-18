package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.wickeddroidmx.plugin.events.game.ChangeGameTimeEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class ChangeGameTimeListener implements Listener {
    
    @Inject
    private GameManager gameManager;
    
    @EventHandler
    public void onChangeGameTime(ChangeGameTimeEvent e) {
        gameManager.setTimeForPvP(e.getPvpTime());
        
        gameManager.setTimeForMeetup(e.getMeetupTime());

        Bukkit.getOnlinePlayers().forEach(player -> {
            player.sendMessage(ChatUtils.PREFIX + ChatUtils.format(String.format("Se ha cambiado el tiempo de pvp a &6%d &7segundos y de meetup a &6%d &7segundos.", e.getPvpTime(), e.getMeetupTime())));

            player.playSound(player.getLocation(), Sound.ENTITY_ARROW_HIT_PLAYER, 1.0F, 1.0F);
        });
    }
}
