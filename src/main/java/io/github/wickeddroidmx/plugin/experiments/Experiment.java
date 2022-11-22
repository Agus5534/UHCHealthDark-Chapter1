package io.github.wickeddroidmx.plugin.experiments;

import io.github.wickeddroidmx.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public abstract class Experiment implements Listener {

    private List<Player> onExperiment;
    private final String key, description;
    private final boolean staff, enabled;

    @Inject
    private Main plugin;

    public Experiment(String key, String description, boolean staff, boolean enabled) {
        this.key = key;
        this.description = description;
        this.staff = staff;
        this.enabled = enabled;

        onExperiment = new ArrayList<>();
    }

    public boolean isStaff() {
        return staff;
    }

    public boolean isOnExperiment(Player player) {
        return onExperiment.contains(player);
    }

    public boolean toggleExperimentStatus(Player player) {
        if(!isOnExperiment(player)) {
            onExperiment.add(player);

            return true;
        } else {
            onExperiment.remove(player);

            return false;
        }
    }

    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    public String getDescription() {
        return description;
    }

    public String getKey() {
        return key;
    }

    public boolean isEnabled() {
        return enabled;
    }
}
