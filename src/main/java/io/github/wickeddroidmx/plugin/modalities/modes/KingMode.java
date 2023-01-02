package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.Objects;

@GameModality(
        modalityType = ModalityType.MODE,
        key = "king",
        name = "&eKing",
        material = Material.GOLD_BLOCK,
        lore = {
                "&7- Se asignará un rey para cada equipo aleatoriamente.",
                "&7- El rey tendrá doble barra de &cvida&7, &4Fuerza I&7 y &8Lentitud I.",
                "&7- Si el rey muere, el resto de su equipo también lo hará."
        }
)
public class KingMode extends Modality {

    @Inject
    private GameManager gameManager;

    @Inject
    private TeamManager teamManager;

    @Inject
    private Main plugin;

    public KingMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        Bukkit.getScheduler().runTaskLater(plugin, ()-> {
            for (var uhcTeam : teamManager.getUhcTeams().values()) {
                var king = uhcTeam.getTeamPlayers()
                        .stream()
                        .map(Bukkit::getPlayer)
                        .filter(Objects::nonNull)
                        .filter(Player::isOnline)
                        .findAny()
                        .orElse(uhcTeam.getOwner());


                Objects.requireNonNull(king.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(40.0D);
                king.setHealth(king.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue());

                uhcTeam.setKing(king);

                Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()-> addEffects(), 1L, 19L);

                Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format(String.format("El king del equipo &6%s &7es &6%s", uhcTeam.getName(), king.getName()))));
            }
        }, 20L);
    }

    private void addEffects() {
        for(var uhcTeam :teamManager.getUhcTeams().values()) {
            if(uhcTeam.getKing() == null) { continue; }
            if(!this.isEnabled()) { continue; }

            var king = uhcTeam.getKing();

            if(!king.isOnline()) { continue; }
            if(king.getGameMode() != GameMode.SURVIVAL) { continue; }

            king.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, 220, 0, false, false, false));
            king.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 220, 0, false, false, false));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        var team = teamManager.getPlayerTeam(player.getUniqueId());

        if (team != null) {
            if (team.getKing().equals(player)) {
                team.getTeamPlayers()
                        .stream()
                        .map(Bukkit::getPlayer)
                        .filter(Objects::nonNull)
                        .filter(Player::isOnline)
                        .forEach(uhcPlayer -> uhcPlayer.setHealth(0.0D));

                Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format("Ha muerto el king del equipo &6" + team.getOwner())));
            }
        }
    }

}
