package io.github.wickeddroidmx.plugin.player;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

public class UhcPlayer {

    private final UUID uuid;
    private final String name;

    private boolean alive,
                    death,
                    scattered,
                    cobbleOnly,
                    chat,
                    spect,
                    extraScoreStats;

    private int kills,
                enemyRecon,
                cobwebs;

    private UhcInventory uhcInventory;

    public UhcPlayer(Player player) {
        this.uuid = player.getUniqueId();

        this.name = player.getName();
        this.kills = 0;
        this.cobwebs = 0;

        this.alive = false;
        this.cobbleOnly = false;
        this.chat = false;
        this.death = false;
        this.spect = false;

        this.scattered = false;
        this.extraScoreStats = false;
        this.uhcInventory = new UhcInventory(player);
    }

    public void setCobbleOnly(boolean cobbleOnly) {
        this.cobbleOnly = cobbleOnly;
    }

    public void setChat(boolean chat) {
        this.chat = chat;
    }

    public void setSpect(boolean spect) {
        this.spect = spect;
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void setDeath(boolean death) {
        this.death = death;
    }

    public void setScattered(boolean scattered) {
        this.scattered = scattered;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void incrementKills() {
        ++this.kills;
    }

    public void incrementCobwebs() { ++this.cobwebs; }

    public boolean isAlive() {
        return alive;
    }

    public boolean isDeath() {
        return death;
    }

    public boolean isCobbleOnly() {
        return cobbleOnly;
    }

    public boolean isChat() {
        return chat;
    }

    public boolean isSpect() {
        return spect;
    }

    public boolean isScattered() {
        return scattered;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public int getKills() {
        return kills;
    }

    public int getCobwebs() {
        return cobwebs;
    }

    public boolean isExtraScoreStats() {
        return extraScoreStats;
    }

    public void setExtraScoreStats(boolean extraScoreStats) {
        this.extraScoreStats = extraScoreStats;
    }

    public void setCobwebs(int cobwebs) {
        this.cobwebs = cobwebs;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public UhcInventory getUhcInventory() {
        return uhcInventory;
    }
}
