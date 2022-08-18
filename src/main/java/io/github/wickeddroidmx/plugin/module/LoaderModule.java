package io.github.wickeddroidmx.plugin.module;

import io.github.wickeddroidmx.plugin.services.CommandLoader;
import io.github.wickeddroidmx.plugin.services.ListenerLoader;
import io.github.wickeddroidmx.plugin.services.Loader;
import io.github.wickeddroidmx.plugin.services.LoaderImpl;
import me.yushust.inject.AbstractModule;

public class LoaderModule extends AbstractModule {

    @Override
    public void configure() {
        bindLoader("main-loader", LoaderImpl.class);
        bindLoader("listener-loader", ListenerLoader.class);
        bindLoader("command-loader", CommandLoader.class);
    }

    private void bindLoader(String name, Class<? extends Loader> loader) {
        bind(Loader.class).named(name).to(loader).singleton();
    }
}
