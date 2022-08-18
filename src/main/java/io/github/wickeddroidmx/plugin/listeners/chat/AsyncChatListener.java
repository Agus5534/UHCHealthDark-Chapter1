package io.github.wickeddroidmx.plugin.listeners.chat;

import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.papermc.paper.event.player.AsyncChatEvent;
import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import javax.inject.Inject;

public class AsyncChatListener implements Listener {

    @Inject
    private PlayerManager playerManager;

    @Inject
    private TeamManager teamManager;

    @EventHandler
    public void onAsyncChat(AsyncPlayerChatEvent e) {
        var player = e.getPlayer();
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());
        var uhcTeam = teamManager.getPlayerTeam(player.getUniqueId());

        if (uhcPlayer == null)
            return;

        if (uhcTeam == null)
            return;

        if (uhcPlayer.isChat()) {
            if (player.getGameMode() == GameMode.SPECTATOR) {
                player.sendMessage(ChatUtils.PREFIX + "No puedes utilizar el chat de equipo una vez muerto.");
                return;
            }

            e.setCancelled(true);

            teamManager.sendMessage(player.getUniqueId(), ChatUtils.format(String.format("%s &8» &7%s", player.getName(), e.getMessage())));
        }
    }
}
