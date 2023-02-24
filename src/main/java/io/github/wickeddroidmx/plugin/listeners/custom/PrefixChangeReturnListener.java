package io.github.wickeddroidmx.plugin.listeners.custom;

import io.github.agus5534.hdbot.minecraft.events.prefixes.PrefixChangeReturnEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PrefixChangeReturnListener implements Listener {

    @EventHandler
    public void onReturn(PrefixChangeReturnEvent event) {
        int code = event.getReturnCode();

        event.getPlayer().sendMessage(ChatUtils.formatComponentPrefix("&6Returned Code " + code + ": " + getReturnCode(code)));
    }

    private String getReturnCode(int code) {
        switch (code) {
            case 0 -> {
                return "&aSe ha cambiado el prefix del usuario exitosamente.";
            }
            case 1 -> {
                return "&4No se ha encontrado el usuario correspondiente a la petición.";
            }
            case 2 -> {
                return "&4El usuario no está habilitado para colocarse este prefix.";
            }
            default -> {
                return "&4Ha ocurrido un error inesperado.";
            }
        }
    }
}
