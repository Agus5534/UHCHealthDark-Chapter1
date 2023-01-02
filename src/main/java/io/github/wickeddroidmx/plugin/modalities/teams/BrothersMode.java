package io.github.wickeddroidmx.plugin.modalities.teams;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameStartEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@GameModality(
        name = "&6Brothers",
        key = "brothers",
        modalityType = ModalityType.TEAM,
        experimental = true,
        material = Material.ROSE_BUSH,
        lore = {
                "&7- To2, uno será hermano mayor y otro menor",
                "&7- El mayor tendrá +5 coras",
                "&7- El menor tendrá Speed I"
        }
)
public class BrothersMode extends Modality {

    @Inject
    private TeamManager teamManager;

    @Inject
    private Main plugin;

    private List<Player> cursedBigBro;
    private List<Player> cursedLittleBro;

    public BrothersMode() throws IllegalClassFormatException {
        super();
    }

    @Override
    public void activeMode() {
        super.activeMode();

        cursedBigBro = new ArrayList<>();
        cursedLittleBro = new ArrayList<>();

        Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()->check(), 120L, 20L);
    }

    @EventHandler
    public void onGameStart(GameStartEvent event) {
        for(var team : teamManager.getUhcTeams().values()) {
            if(team.getTeamPlayers().size() == 1) {
                team.getTeamPlayers().stream()
                                .map(Bukkit::getPlayer)
                                        .filter(player -> player.isOnline())
                                                .forEach(p -> p.setHealth(0.0D));

                continue;
            }

            var pl = team.getTeamPlayers().stream().map(Bukkit::getPlayer)
                    .sorted()
                    .collect(Collectors.toList());

            var pl1 = pl.get(0);
            var pl2 = pl.get(1);

            team.setBigbrother(pl1);
            team.setLittlebrother(pl2);

            pl1.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(30.0D);
            pl1.setHealth(30.0D);

            var defaultValue = pl2.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getDefaultValue();

            pl2.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(defaultValue+(defaultValue*0.20));
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {
        var team = teamManager.getTeam(event.getEntity().getUniqueId());

        if(team == null) { return; }

        var bigBro = team.getBigbrother();
        var littleBro = team.getLittlebrother();

        if(bigBro == null || littleBro == null) { return; }

        var victim = event.getEntity();

        if(victim.getUniqueId().equals(bigBro.getUniqueId())) {
            cursedLittleBro.add(littleBro);
        }

        if(victim.getUniqueId().equals(littleBro.getUniqueId())) {
            cursedBigBro.add(bigBro);
        }
    }

    private void check() {
        for(var p : cursedBigBro) {
            if(!p.isOnline()) { continue; }

            var defaultValue = p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).getDefaultValue();

            p.getAttribute(Attribute.GENERIC_MOVEMENT_SPEED).setBaseValue(defaultValue*0.85);
        }

        for(var p : cursedLittleBro) {
            if(!p.isOnline()) { continue; }

            var attr = p.getAttribute(Attribute.GENERIC_MAX_HEALTH).getBaseValue();

            if(attr != 17.0) {
                p.getAttribute(Attribute.GENERIC_MAX_HEALTH).setBaseValue(17.0);
            }
        }
    }
}
