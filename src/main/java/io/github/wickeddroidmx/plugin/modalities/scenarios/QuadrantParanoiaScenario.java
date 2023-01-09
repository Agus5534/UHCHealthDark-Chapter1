package io.github.wickeddroidmx.plugin.modalities.scenarios;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.player.PlayerManager;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.text.format.TextColor;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&6Quadrant Paranoia",
        modalityType = ModalityType.SCENARIO,
        key = "quedrant_paranoia",
        material = Material.COMPASS,
        lore = {"&7- Los cuadrantes salen de nombre de la persona"}
)
public class QuadrantParanoiaScenario extends Modality {
    @Inject
    private Main plugin;
    @Inject
    private TeamManager teamManager;
    @Inject
    private PlayerManager playerManager;

    public QuadrantParanoiaScenario() throws IllegalClassFormatException {
        super();
    }

    @EventHandler
    public void onGameTick(GameTickEvent event) {
        Bukkit.getScheduler().runTask(plugin, ()-> Bukkit.getOnlinePlayers().forEach(this::updateName));
    }

    private void updateName(Player player) {
        var uhcTeam = teamManager.getPlayerTeam(player.getUniqueId());
        var uhcPlayer = playerManager.getPlayer(player.getUniqueId());

        if(uhcTeam == null || uhcPlayer == null) {
            player.playerListName(null);
            return;
        }

        if(!uhcPlayer.isAlive()) {
            player.playerListName(null);
            return;
        }

        var color = uhcTeam.getTeam().color();
        var playerLoc = player.getLocation();

        var displayName = ChatUtils.formatC("")
                .append(
                        uhcTeam.getTeam().prefix().color(color)
                )
                .append(ChatUtils.formatC(
                        String.format("%s | ", getQuadrant(playerLoc.getX(), playerLoc.getZ()))
                ).color(color))
                .append(ChatUtils.formatC(
                        String.format("%s", player.getName())
                ).color(color))
                .append(
                        uhcTeam.getTeam().suffix().color(color)
                );

        player.playerListName(displayName);
    }

    private String getQuadrant(double X, double Z) {
        String s = "";

        s+=  Integer.signum((int)X) < 0 ? "-" : "+";
        s+=  Integer.signum((int)Z) < 0 ? "-" : "+";

        return s;
    }
}
