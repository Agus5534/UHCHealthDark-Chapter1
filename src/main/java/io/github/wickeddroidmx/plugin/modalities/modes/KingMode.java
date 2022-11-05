package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamScatteredEvent;
import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
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

    public KingMode() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameStart(GameStartEvent e) {
        for (var uhcTeam : teamManager.getUhcTeams().values()) {
            var king = uhcTeam.getTeamPlayers()
                    .stream()
                    .map(Bukkit::getPlayer)
                    .filter(Objects::nonNull)
                    .filter(Player::isOnline)
                    .findAny()
                    .orElse(uhcTeam.getOwner());


            Objects.requireNonNull(king.getAttribute(Attribute.GENERIC_MAX_HEALTH)).setBaseValue(40.0D);
            king.setHealth(40.0D);

            king.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 0));
            king.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, Integer.MAX_VALUE, 0));

            uhcTeam.setKing(king);

            Bukkit.broadcast(Component.text(ChatUtils.PREFIX + ChatUtils.format(String.format("El king del equipo &6%s &7es &6%s", uhcTeam.getName(), king.getName()))));
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent e) {
        var player = e.getEntity();
        var team = teamManager.getPlayerTeam(player.getUniqueId());

        if (team != null) {
            if (team.getKing() == player) {
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

    @EventHandler
    public void onPlayerItemConsume(PlayerItemConsumeEvent e) {

    }
}
