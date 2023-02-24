package io.github.wickeddroidmx.plugin.services;

import io.github.wickeddroidmx.plugin.experiments.ExperimentManager;
import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import me.yushust.inject.InjectAll;
import org.bukkit.Bukkit;

@InjectAll
public class LoaderImpl implements Loader {

    private ListenerLoader listenerLoader;
    private CommandLoader commandLoader;
    private UhcIdLoader uhcIdLoader;
    private ModeManager modeManager;
    private ExperimentManager experimentManager;

    @Override
    public void load() {
        listenerLoader.load();
        commandLoader.load();
        uhcIdLoader.load();

        modeManager.registerModes();
        experimentManager.registerExperiments();

        Bukkit.getLogger().info("Plugin prendido correctamente.");
    }
}
