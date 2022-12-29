package io.github.wickeddroidmx.plugin.listeners.chat;

import io.github.agus5534.hdbot.minecraft.events.SyncPlayerChatEvent;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import javax.inject.Inject;

public class AsyncChatListener implements Listener {

    @Inject
    private PlayerManager playerManager;

    @Inject
    private TeamManager teamManager;

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onSyncChat(SyncPlayerChatEvent e) {
        var player = e.getPlayer();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());
        var uhcTeam = teamManager.getPlayerTeam(player.getUniqueId());

        if (uhcTeam != null && uhcPlayer != null) {
            if(e.getMessage().startsWith("!")) {
                String format = "%s &8» &7%s";

                if(uhcTeam.containsFlag(TeamFlags.COLORED_OWNER_TEAMCHAT) && uhcTeam.getOwner().equals(player)) {
                    format = "&6%s &8» &7%s";
                }

                Component msg = !(uhcPlayer.isChat())
                        ? ChatUtils.formatC("").append(e.getPrefixes()).append(ChatUtils.formatC(String.format(format, player.getName(), e.getMessage().replaceFirst("!", ""))))
                        : ChatUtils.formatC("").append(e.getPrefixes()).append(ChatUtils.formatC(String.format(e.getFormat(), player.getName(), e.getMessage().replaceFirst("!", ""))));

                if(uhcPlayer.isChat()) {
                    Bukkit.broadcast(msg);
                } else {
                    if (player.getGameMode() == GameMode.SPECTATOR || uhcPlayer.isDeath() || uhcTeam.containsFlag(TeamFlags.BLOCK_TEAM_CHAT)) {
                        player.sendMessage(ChatUtils.PREFIX + "No puedes utilizar el chat de equipo en este momento.");

                        uhcPlayer.setChat(false);

                        return;
                    }

                    e.setCancelled(true);
                    teamManager.sendMessage(player.getUniqueId(), msg);
                }

                return;
            }

            if(uhcPlayer.isChat()) {
                if (player.getGameMode() == GameMode.SPECTATOR || uhcPlayer.isDeath() || uhcTeam.containsFlag(TeamFlags.BLOCK_TEAM_CHAT)) {
                    player.sendMessage(ChatUtils.PREFIX + "No puedes utilizar el chat de equipo en este momento.");

                    uhcPlayer.setChat(false);

                    return;
                }

                e.setCancelled(true);

                String format = "%s &8» &7%s";

                if(uhcTeam.containsFlag(TeamFlags.COLORED_OWNER_TEAMCHAT) && uhcTeam.getOwner().equals(player)) {
                    format = "&6%s &8» &7%s";
                }

                Component hover = ChatUtils.formatC("&e⚠ Aviso\n&bSe puede enviar mensajes al otro chat sin cambiarlo iniciando el mensaje con \"!\"");
                Component msg = Component.text("").append(e.getPrefixes()).append(ChatUtils.formatC(String.format(format, player.getName(), e.getMessage()))).hoverEvent(hover);
                teamManager.sendMessage(player.getUniqueId(), msg);
                Bukkit.getLogger().info(String.format("[TEAMCHAT] [%s] %s » %s", uhcTeam.getName(), player.getName(), e.getMessage()));

                return;
            }
        }

        Component message = Component.text("").append(e.getPrefixes()).append(Component.text(String.format(e.getFormat(), e.getPlayer().getName(), e.getMessage())));

        Bukkit.broadcast(message);
    }
}
