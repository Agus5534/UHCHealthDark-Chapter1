package io.github.wickeddroidmx.plugin.utils.runnable;

import io.github.wickeddroidmx.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.function.Predicate;

public class ListenerWatcher implements Listener {
    private final JavaPlugin plugin;
    private final Class<?> event;
    private final Predicate<Event> eventPredicate;

    public ListenerWatcher(JavaPlugin plugin, Class<?> event, Predicate<Event> eventPredicate) {
        this.plugin = plugin;
        this.event = event;
        this.eventPredicate = eventPredicate;
    }

    public void unregisterListener() {
        HandlerList.unregisterAll(this);
    }

    public void registerListener() {
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onEvent(Event event) {
        if(!event.getEventName().equalsIgnoreCase(this.event.getSimpleName())) {
            return;
        }

        if(eventPredicate.test(event)) {
            unregisterListener();
        }
    }
}
