package io.github.wickeddroidmx.plugin.modalities.teams;

import io.github.wickeddroidmx.plugin.events.team.PlayerJoinedTeamEvent;
import io.github.wickeddroidmx.plugin.events.team.TeamCreateEvent;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import javax.inject.Inject;

public class LoveAtFirstSightMode extends Modality {

    @Inject
    private TeamManager teamManager;

    public LoveAtFirstSightMode() {
        super(ModalityType.TEAM, "lafs", "&cLove At First Sight", Material.POPPY,
                ChatUtils.format("&7- Encontrarás a tu team en el curso de la partida."));
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent e) {
        if (e.getEntity() instanceof Player player
                && e.getDamager() instanceof Player damager) {
            var playerTeam = teamManager.getPlayerTeam(player.getUniqueId());

            Bukkit.getLogger().info("Hola2");

            if (playerTeam != null)
                return;

            Bukkit.getLogger().info("Hola");

            if (teamManager.getPlayerTeam(damager.getUniqueId()) == null) {
                teamManager.createTeam(player);

                Bukkit.getPluginManager().callEvent(new TeamCreateEvent(teamManager.getPlayerTeam(damager.getUniqueId()), player));
                Bukkit.getLogger().info("Hola3");
            }

            var team = teamManager.getPlayerTeam(damager.getUniqueId());

            Bukkit.getLogger().info("Hol4");

            if (team.getSize() == teamManager.getTeamSize())
                return;

            Bukkit.getLogger().info("Hola5");

            Bukkit.getPluginManager().callEvent(new PlayerJoinedTeamEvent(team, player));

            Bukkit.broadcast(Component.text(ChatUtils.PREFIX + String.format(ChatUtils.format("El usuario &6%s &7se ha unido al team de &6%s"), damager.getName(), team.getName())));
        }
    }
}
