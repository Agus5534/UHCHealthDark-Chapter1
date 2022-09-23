package io.github.wickeddroidmx.plugin.teams;

import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.scoreboard.Team;

import java.util.*;

public class UhcTeam {

    private final int id;
    private final Set<UUID> teamPlayers;

    private final Team team;
    private String name;

    private final Inventory inventory;

    private Player owner,
            mole,
            king;

    private boolean alive;

    private NamedTextColor[] textColors = {
            NamedTextColor.AQUA,
            NamedTextColor.BLUE,
            NamedTextColor.DARK_AQUA,
            NamedTextColor.DARK_BLUE,
            NamedTextColor.DARK_GRAY,
            NamedTextColor.DARK_RED,
            NamedTextColor.YELLOW,
            NamedTextColor.LIGHT_PURPLE,
            NamedTextColor.RED,
            NamedTextColor.GREEN,
            NamedTextColor.GRAY
    };

    private int kills,
            size,
            playersAlive;

    public UhcTeam(Player owner, int id) {
        this.teamPlayers = new HashSet<>();
        this.mole = null;
        this.king = null;

        this.alive = true;

        this.owner = owner;
        this.playersAlive = 0;

        this.name = String.format("%s team",owner.getName());

        this.inventory = Bukkit.createInventory(null, 27, Component.text(ChatUtils.format("&bInventario de " + name)));
        this.id = id;
        this.size = 0;

        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(owner.getName()) == null) {
            this.team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(owner.getName());

            team.prefix(Component.text(String.format("[Team %s] | ", owner.getName())));
            team.color(textColors[new Random().nextInt(textColors.length)]);
            team.setAllowFriendlyFire(false);
        } else
            this.team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(owner.getName());

        addPlayer(owner);
    }

    public void addPlayer(Player player) {
        var uuid = player.getUniqueId();

        ++this.size;
        ++this.playersAlive;

        team.addEntry(player.getName());

        teamPlayers.add(uuid);
    }

    public void setPrefix(String text) {
        team.prefix(Component.text(String.format("%s | ", text)));
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void leavePlayer(Player player) {
        var uuid = player.getUniqueId();

        --this.size;

        team.removeEntry(player.getName());

        teamPlayers.remove(uuid);
    }

    public boolean existsPlayer(UUID uuid) {
        return teamPlayers.contains(uuid);
    }

    public void setAlive(boolean alive) {
        this.alive = alive;
    }

    public void decrementPlayersAlive() {
        --this.playersAlive;
    }

    public void incrementPlayersAlive() {
        ++this.playersAlive;
    }

    public void setMole(Player mole) {
        this.mole = mole;
    }

    public void setKing(Player king) {
        this.king = king;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public void setKills(int kills) {
        this.kills = kills;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSize(int size) {
        this.size = size;
    }

    public void incrementKills() {
        ++this.kills;
    }

    public int getId() {
        return id;
    }

    public int getPlayersAlive() {
        return playersAlive;
    }

    public int getSize() {
        return size;
    }

    public boolean isAlive() {
        return alive;
    }

    public Player getOwner() {
        return owner;
    }

    public Set<UUID> getTeamPlayers() {
        return teamPlayers;
    }

    public int getKills() {
        return kills;
    }

    public Player getMole() {
        return mole;
    }

    public Team getTeam() {
        return team;
    }

    public Player getKing() {
        return king;
    }

    public String getName() {
        return name;
    }
}
