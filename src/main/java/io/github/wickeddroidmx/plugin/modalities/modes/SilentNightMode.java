package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import io.github.wickeddroidmx.plugin.teams.TeamFlags;
import io.github.wickeddroidmx.plugin.teams.TeamManager;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.scoreboard.DisplaySlot;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;

@GameModality(
        name = "&9Silent &8Night",
        modalityType = ModalityType.MODE,
        key = "silent_night",
        material = Material.BLUE_BED,
        lore = {"&7- La Noche y el Día alteran el juego"},
        experimental = true
)
public class SilentNightMode extends Modality {

    @Inject
    private Main plugin;

    @Inject
    private TeamManager teamManager;

    private DayLight dayLightStatus;

    private int taskID = 0;

    public SilentNightMode() throws IllegalClassFormatException {
        super();
    }


    public DayLight getGameDayLight() {
        var ticks = plugin.getWorldGenerator().getUhcWorld().getWorld().getTime();
        return ((ticks > 13000 && ticks < 23000) ? DayLight.NIGHT : DayLight.DAY);
    }

    @Override
    public void activeMode() {
        super.activeMode();
        taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, ()->updateDayLightStatus(), 1L,5L);
    }

    @Override
    public void deactivateMode() {
        super.deactivateMode();
        if(Bukkit.getScheduler().isCurrentlyRunning(taskID)) {
            Bukkit.getScheduler().cancelTask(taskID);
        }
    }

    private void updateDayLightStatus() {
        if(getDayLightStatus() != getGameDayLight() && this.isEnabled()) {
            setDayLightStatus(getGameDayLight());

            if(getDayLightStatus() == DayLight.NIGHT) {
                updateToNight();
                Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + "Ha comenzado la noche..."));
            } else {
                updateToDay();
                Bukkit.broadcast(ChatUtils.formatC(ChatUtils.PREFIX + "La noche ha terminado, un nuevo día comienza."));
            }
        }
    }

    private void updateToNight() {
        Bukkit.getScoreboardManager().getMainScoreboard().clearSlot(DisplaySlot.PLAYER_LIST);

        for(var t : teamManager.getUhcTeams().values()) {
            addTeamFlag(t, TeamFlags.HIDE_TAB_NICKNAMES);
        }
    }

    private void updateToDay() {
        Bukkit.getScoreboardManager().getMainScoreboard().getObjective("health").setDisplaySlot(DisplaySlot.PLAYER_LIST);

        for(var t : teamManager.getUhcTeams().values()) {
            removeTeamFlag(t, TeamFlags.HIDE_TAB_NICKNAMES);
        }
    }

    public enum DayLight {
        NIGHT,
        DAY
    }

    public DayLight getDayLightStatus() {
        return dayLightStatus;
    }

    private void addTeamFlag(UhcTeam team, TeamFlags... flags) {
        for(var f : flags) {
            if(!team.containsFlag(f)) {
                team.addFlag(f);
            }
        }
    }

    private void removeTeamFlag(UhcTeam team, TeamFlags... flags) {
        for(var f : flags) {
            if(team.containsFlag(f)) {
                team.removeFlag(f);
            }
        }
    }

    public void setDayLightStatus(DayLight dayLightStatus) {
        this.dayLightStatus = dayLightStatus;
    }
}
