package io.github.wickeddroidmx.plugin.player;

import org.bukkit.entity.Player;

import javax.inject.Singleton;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Singleton
public class PlayerManager {

    private final HashMap<UUID, UhcPlayer> uhcPlayerMap = new HashMap<>();

    public void createPlayer(Player player, boolean alive) {
        var uhcPlayer = new UhcPlayer(player);
        uhcPlayer.setAlive(alive);

        uhcPlayerMap.put(player.getUniqueId(), uhcPlayer);
    }

    public UhcPlayer getPlayer(UUID uuid) {
        return uhcPlayerMap.get(uuid);
    }

    public void deletePlayer(Player player) {
        uhcPlayerMap.remove(player.getUniqueId());
    }

    public boolean existsPlayer(Player player) {
        return uhcPlayerMap.containsKey(player.getUniqueId());
    }

    public List<Player> getPlayersAlive() {
        return uhcPlayerMap.values()
                .stream()
                .filter(UhcPlayer::isAlive)
                .map(UhcPlayer::getPlayer)
                .collect(Collectors.toList());
    }

    public Map<UUID, Integer> getKillTop() {
        return uhcPlayerMap.values()
                .stream()
                .filter(uhcPlayer -> uhcPlayer.getKills() > 0)
                .collect(Collectors.toMap(UhcPlayer::getUuid, UhcPlayer::getKills));
    }

    public HashMap<UUID, UhcPlayer> getUhcPlayers() {
        return uhcPlayerMap;
    }
}
