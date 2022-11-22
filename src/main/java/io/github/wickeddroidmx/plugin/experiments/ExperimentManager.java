package io.github.wickeddroidmx.plugin.experiments;

import io.github.wickeddroidmx.plugin.experiments.list.GAME_ARENA;
import me.yushust.inject.InjectAll;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import javax.inject.Singleton;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

@InjectAll
@Singleton
public class ExperimentManager {
    private GAME_ARENA game_arena;

    public void registerExperiments() {
        registerExperiments(
                game_arena
        );
    }

    private final Set<Experiment> experimentSet = new HashSet<>();
    private final HashMap<String, Experiment> stringExperimentHashMap = new HashMap<>();

    private void registerExperiments(Experiment... experiments) {
        Arrays.stream(experiments).forEach(e -> {
            experimentSet.add(e);

            stringExperimentHashMap.put(e.getKey(), e);

            e.onEnable();
        });
    }

    public boolean hasExperiment(Player player, String key) {
        return getExperimentByKey(key).isOnExperiment(player);
    }

    public Set<Experiment> getExperiments() {
        return experimentSet;
    }

    public Experiment getExperimentByKey(String key) {
        return stringExperimentHashMap.get(key);
    }
}
