package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;
import java.util.Objects;
import java.util.Random;

public class JumperMode extends Modality {

    @Inject
    private GameManager gameManager;

    @Inject
    private TeamManager teamManager;

    public JumperMode() {
        super(ModalityType.MODE, "jumpers", "&cJumpers", Material.LEATHER_BOOTS,
                ChatUtils.format("&7- Cada 15 minutos se tepea a los jugadores a una ubicación aleatoria."));
    }

    @EventHandler
    public void onGameTick(GameTickEvent e) {
        var random = new Random();

        if (e.getTime() % (60 * 15) == 0) {
            for (UhcTeam uhcTeam : teamManager.getUhcTeams().values()) {
                var world = Bukkit.getWorld("uhc_world");
                var x = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());
                var z = -(gameManager.getWorldBorder() / 2) + random.nextInt(gameManager.getWorldBorder());

                if (world != null) {
                    var location = new Location(world, x, world.getHighestBlockYAt(x, z), z);

                    uhcTeam.getTeamPlayers()
                            .stream()
                            .map(Bukkit::getPlayer)
                            .filter(Objects::nonNull)
                            .filter(Player::isOnline)
                            .forEach(player -> player.teleport(location));
                }
            }
        }
    }
}
