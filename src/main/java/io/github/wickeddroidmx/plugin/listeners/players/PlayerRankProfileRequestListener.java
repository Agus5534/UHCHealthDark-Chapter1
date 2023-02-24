package io.github.wickeddroidmx.plugin.listeners.players;

import io.github.agus5534.hdbot.Ranks;
import io.github.agus5534.hdbot.minecraft.events.PlayerRankProfileRequestEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.HashMap;

public class PlayerRankProfileRequestListener implements Listener {

    public static HashMap<Player, Ranks> playerRanksHashMap = new HashMap<>();

    @EventHandler
    public void onPlayerRankProfileRequest(PlayerRankProfileRequestEvent event) {
        if(event.getType() == PlayerRankProfileRequestEvent.TYPE.REQUEST) { return; }
        if(event.getRanks() == null) { return; }

        if(playerRanksHashMap.containsKey(event.getPlayer())) {
            playerRanksHashMap.remove(event.getPlayer());
        }

        playerRanksHashMap.put(event.getPlayer(), event.getRanks());
    }
}
