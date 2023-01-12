package io.github.wickeddroidmx.plugin.utils.runnable;

import io.github.wickeddroidmx.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.event.Event;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.function.Predicate;

public class TaskCreator {
    private final Main plugin;

    public TaskCreator(Main plugin) {
        this.plugin = plugin;
    }

    public BukkitTask runSync(Runnable runnable) {
        return Bukkit.getScheduler().runTask(plugin, runnable);
    }

    public BukkitTask runSyncScheduled(Runnable runnable, long delay) {
        return Bukkit.getScheduler().runTaskLater(plugin, runnable, delay);
    }

    public BukkitTask registerListenerWatcher(ListenerWatcher listenerWatcher) {
        return Bukkit.getScheduler().runTask(plugin, listenerWatcher::registerListener);
    }

    public BukkitTask registerListenerWatcher(Class<?> eventClass, Predicate<Event> event) {
        ListenerWatcher listenerWatcher = new ListenerWatcher(plugin, eventClass, event);

        return this.registerListenerWatcher(listenerWatcher);
    }

    public void endTask(int task) {
        Bukkit.getScheduler().cancelTask(task);
    }

    public int registerSyncRepeatingScheduled(Runnable runnable, long startDelay, long delay) {
        return Bukkit.getScheduler().scheduleSyncRepeatingTask(plugin, runnable, startDelay, delay);
    }
}
