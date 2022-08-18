package io.github.wickeddroidmx.plugin.events.worldborder;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

public class WorldBorderSetEvent extends Event {

    private final int worldBorder;
    private final Player player;

    private static final HandlerList HANDLER_LIST = new HandlerList();

    public WorldBorderSetEvent(int worldBorder, Player player) {
        this.worldBorder = worldBorder;
        this.player = player;
    }

    public WorldBorderSetEvent(int worldBorder) {
        this(worldBorder, null);
    }

    public int getWorldBorder() {
        return worldBorder;
    }

    public Player getPlayer() {
        return player;
    }

    public static HandlerList getHandlerList() {
        return HANDLER_LIST;
    }

    @Override
    public @NotNull HandlerList getHandlers() {
        return HANDLER_LIST;
    }
}
