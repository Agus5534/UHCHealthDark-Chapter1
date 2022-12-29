package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.agus5534.hdbot.minecraft.events.prefixes.PrefixChangeReturnEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PrefixChangeReturnListener implements Listener {

    @EventHandler
    public void onReturn(PrefixChangeReturnEvent event) {
        event.getPlayer().sendMessage(ChatUtils.formatComponentPrefix(event.getMessage()));
    }
}
