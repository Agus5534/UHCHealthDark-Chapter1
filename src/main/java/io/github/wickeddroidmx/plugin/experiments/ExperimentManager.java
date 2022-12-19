package io.github.wickeddroidmx.plugin.experiments;

import io.github.wickeddroidmx.plugin.experiments.list.COBWEB_WARN_EXPERIMENT;
import io.github.wickeddroidmx.plugin.experiments.list.GAME_ARENA;
import io.github.wickeddroidmx.plugin.experiments.list.IRONMAN_COMMAND;
import me.yushust.inject.InjectAll;
import me.yushust.inject.InjectIgnore;
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
    private IRONMAN_COMMAND ironman_command;
    private COBWEB_WARN_EXPERIMENT cobweb_warn_experiment;

    public void registerExperiments() {
        registerExperiments(
                game_arena,
                ironman_command,
                cobweb_warn_experiment
        );
    }

    @InjectIgnore
    private static final Set<Experiment> experimentSet = new HashSet<>();
    @InjectIgnore
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

    public static Set<Experiment> getExperiments() {
        return experimentSet;
    }

    public Experiment getExperimentByKey(String key) {
        return stringExperimentHashMap.get(key);
    }

    public HashMap<String, Experiment> getStringExperimentHashMap() {
        return stringExperimentHashMap;
    }
}
