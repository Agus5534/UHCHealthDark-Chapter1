package io.github.wickeddroidmx.plugin.services;

import io.github.wickeddroidmx.plugin.modalities.ModeManager;
import me.yushust.inject.InjectAll;
import org.bukkit.Bukkit;

@InjectAll
public class LoaderImpl implements Loader {

    private ListenerLoader listenerLoader;
    private CommandLoader commandLoader;
    private ModeManager modeManager;

    @Override
    public void load() {
        listenerLoader.load();
        commandLoader.load();

        modeManager.registerModes();

        Bukkit.getLogger().info("Plugin prendido correctamente.");
    }
}
