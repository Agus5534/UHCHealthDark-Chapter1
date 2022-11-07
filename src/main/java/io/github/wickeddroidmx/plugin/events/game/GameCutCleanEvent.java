package io.github.wickeddroidmx.plugin.events.game;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.block.BlockExpEvent;
import org.jetbrains.annotations.NotNull;

public class GameCutCleanEvent extends BlockExpEvent implements Cancellable {
    private final Player player;
    private boolean dropItems;
    private boolean cancel;

    public GameCutCleanEvent(@NotNull Block theBlock, @NotNull Player player) {
        super(theBlock, 0);
        this.player = player;
        this.dropItems = true;
    }

    public @NotNull Player getPlayer() {
        return this.player;
    }

    public void setDropItems(boolean dropItems) {
        this.dropItems = dropItems;
    }

    public boolean isDropItems() {
        return this.dropItems;
    }

    public boolean isCancelled() {
        return this.cancel;
    }

    public void setCancelled(boolean cancel) {
        this.cancel = cancel;
    }
}
