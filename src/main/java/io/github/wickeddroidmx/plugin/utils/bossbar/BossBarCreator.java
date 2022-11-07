package io.github.wickeddroidmx.plugin.utils.bossbar;

import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public class BossBarCreator {
    private final BossBar bossBar;
    private Component name;

    private BossBar.Color color;

    private BossBar.Overlay overlay;

    private final JavaPlugin javaPlugin;

    private int taskID;

    private float progress;

    public BossBarCreator(JavaPlugin javaPlugin, String name, BossBar.Color color, BossBar.Overlay overlay, float progress) {
        this.javaPlugin = javaPlugin;
        this.overlay = overlay;
        this.color = color;
        this.progress = progress;
        this.name = ChatUtils.formatC(name);

        bossBar = BossBar.bossBar(this.name,progress,this.color,overlay);

        taskID = javaPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(javaPlugin, ()-> updateBossbar(), 1L, 1L);
    }

    public void updateBossbar() {
        bossBar.name(getName());
        bossBar.overlay(getOverlay());
        bossBar.color(getColor());
        bossBar.progress(getProgress());
    }

    public BossBar getBossBar() {
        return bossBar;
    }

    public Component getName() {
        return name;
    }

    public void setName(Component name) {
        this.name = name;
    }

    public BossBar.Color getColor() {
        return color;
    }

    public void setColor(BossBar.Color color) {
        this.color = color;
    }

    public BossBar.Overlay getOverlay() {
        return overlay;
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
    }

    public void setOverlay(BossBar.Overlay overlay) {
        this.overlay = overlay;
    }

    public void addListener(BossBar.Listener listener) {
        this.bossBar.addListener(listener);
    }

    public void removeListener(BossBar.Listener listener) {
        this.bossBar.removeListener(listener);
    }

    public void addFlags(BossBar.Flag... flags) {
        bossBar.addFlags(flags);
    }

    public void removeFlags(BossBar.Flag... flags) {
        bossBar.removeFlags(flags);
    }

    public int getTaskID() {
        return taskID;
    }

    private void setTaskID(int taskID) {
        this.taskID = taskID;
    }

    public boolean isRunningTask() {
        return javaPlugin.getServer().getScheduler().isCurrentlyRunning(taskID);
    }

    public void toggleTask() {
        if(isRunningTask()) {
            javaPlugin.getServer().getScheduler().cancelTask(getTaskID());
        } else {
            setTaskID(javaPlugin.getServer().getScheduler().scheduleSyncRepeatingTask(
                    javaPlugin,
                    ()->updateBossbar(),
                    1L,
                    1L
            ));
        }
    }

    public void showBossBar(Player... players) {
        Arrays.stream(players).forEach(p -> p.showBossBar(getBossBar()));
    }

    public void hideBossBar(Player... players) {
        Arrays.stream(players).forEach(p -> p.hideBossBar(getBossBar()));
    }
}
