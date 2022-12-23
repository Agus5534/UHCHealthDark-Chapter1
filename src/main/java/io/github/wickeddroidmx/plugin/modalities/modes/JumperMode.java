package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.Main;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Objects;
import java.util.Random;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "jumpers",
        name = "&cJumpers",
        material = Material.LEATHER_BOOTS,
        lore = {"&7- Cada 15 minutos se teletransporta a todos a una ubicación al azar"}
)
public class JumperMode extends Modality {

    @Inject
    private GameManager gameManager;

    @Inject
    private TeamManager teamManager;
    @Inject
    private Main plugin;

    int delay = 0;

    public JumperMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameTick(GameTickEvent e) {
         if (e.getTime() % 900 == 0 && e.getTime() > 1) {
            delay = 1;

            for (UhcTeam uhcTeam : teamManager.getUhcTeams().values()) {
                uhcTeam.getTeamPlayers().forEach(u -> {
                    var player = Bukkit.getOfflinePlayer(u);

                    if(!player.isOnline()) { return; }

                    if(player.getPlayer().getGameMode() == GameMode.SPECTATOR) { return; }

                    Bukkit.getScheduler().runTaskLater(plugin, ()-> teleportPlayer(player.getPlayer()), delay);

                    delay = delay + 10;
                });

            }
        }
    }

    private void teleportPlayer(Player player) {
        var random = new Random();
        var wb = player.getLocation().getWorld().getWorldBorder().getSize() / 2;

        if(wb > 2000) { wb = 1000; }

        var x = -(gameManager.getWorldBorder() / 2) + random.nextInt((int) wb);
        var z = -(gameManager.getWorldBorder() / 2) + random.nextInt((int) wb);

        var location = new Location(player.getWorld(), x, 170, z);

        player.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, player.getWorld().getEnvironment() == World.Environment.NETHER ? 800 : 400, 20, false, false, false));
        player.teleport(location);
    }
}
