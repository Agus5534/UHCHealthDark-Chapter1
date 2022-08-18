package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.game.GameManager;
import io.github.wickeddroidmx.plugin.game.GameState;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;

import javax.inject.Inject;

public class ChickenFightScenario extends Modality {

    @Inject
    private TeamManager teamManager;

    @Inject
    private GameManager gameManager;

    public ChickenFightScenario() {
        super(ModalityType.SCENARIO, "chicken_fight", "&eChicken Fight", Material.EGG,
                ChatUtils.format("&7- Puedes subirte arriba de tus compañeros."));
    }

    @EventHandler
    public void onPlayerInteractAtEntity(PlayerInteractAtEntityEvent e) {
        if (gameManager.getGameState() == GameState.WAITING) return;

        var player = e.getPlayer();
        var entity = e.getRightClicked();


        if (entity instanceof Player playerInteracted) {
            var team = teamManager.getPlayerTeam(playerInteracted.getUniqueId());

            if (team != null) {

                if (team.getTeamPlayers().contains(player.getUniqueId()))
                    playerInteracted.addPassenger(player);
            }
        }
    }
}
