package io.github.wickeddroidmx.plugin.modalities.modes;

import io.github.wickeddroidmx.plugin.Main;
import io.github.wickeddroidmx.plugin.events.game.GameTickEvent;
import io.github.wickeddroidmx.plugin.modalities.GameModality;
import io.github.wickeddroidmx.plugin.modalities.Modality;
import io.github.wickeddroidmx.plugin.modalities.ModalityType;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;

import javax.inject.Inject;
import java.lang.instrument.IllegalClassFormatException;
import java.util.concurrent.ThreadLocalRandom;

@GameModality(
        name = "&8Crazy DayLight Cycle",
        modalityType = ModalityType.MODE,
        key = "crazy_daylight",
        experimental = true,
        lore = {"&7- El día y la noche podrían variar de diferente manera"},
        material = Material.CLOCK
)
public class CrazyDayLightCycleMode extends Modality {

    public CrazyDayLightCycleMode() throws IllegalClassFormatException {
        super();
    }

    @Inject
    private Main plugin;
    private ACTION action;
    private long tickToModify;
    private int chancePerTick;

    @Override
    public void activeMode() {
        super.activeMode();

        setAction(ACTION.NORMAL);
        tickToModify = ThreadLocalRandom.current().nextInt(1, 10);
    }

    @EventHandler
    public void onGameTick(GameTickEvent event) {
        if(event.getTime() % 300 == 0) {
            changeAction();
        }

        Bukkit.getScheduler().runTask(plugin, () -> {
            if(getAction() == ACTION.SPEED) {
                Bukkit.getWorlds().forEach(w -> w.setFullTime(w.getFullTime() + tickToModify));
            }

            if(getAction() == ACTION.SLOW) {
                if(chancePerTick > ThreadLocalRandom.current().nextInt(1, 250)) {
                    Bukkit.getWorlds().forEach(w -> w.setFullTime(w.getFullTime() - tickToModify));
                }
            }
        });

    }

    enum ACTION {
        NORMAL,
        SPEED,
        SLOW
    }

    public ACTION getAction() {
        return action;
    }

    private void changeAction() {
        int n = ThreadLocalRandom.current().nextInt(0, 100);

        if(n < 10) {
            setAction(ACTION.NORMAL);
        } else if(n < 55) {
            setAction(ACTION.SPEED);
        } else {
            setAction(ACTION.SLOW);
        }

        tickToModify = ThreadLocalRandom.current().nextInt(1, 7);
        chancePerTick = ThreadLocalRandom.current().nextInt(2, 45);
    }

    public void setAction(ACTION action) {
        this.action = action;
    }
}
