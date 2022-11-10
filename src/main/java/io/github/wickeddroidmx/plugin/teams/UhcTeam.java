package io.github.wickeddroidmx.plugin.teams;

import io.github.wickeddroidmx.plugin.events.team.TeamFlagChangedEvent;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
    private boolean alive, friendlyFire;

    private ChatColor color;

    private List<TeamFlags> teamFlags;

    private ChatColor[] textColors = {
            ChatColor.AQUA,
            ChatColor.BLUE,
            ChatColor.DARK_AQUA,
            ChatColor.DARK_BLUE,
            ChatColor.DARK_GRAY,
            ChatColor.DARK_RED,
            ChatColor.YELLOW,
            ChatColor.LIGHT_PURPLE,
            ChatColor.RED,
            ChatColor.GREEN,
            ChatColor.GRAY
    };

    private int kills,
            size,
            playersAlive;

    public UhcTeam(Player owner, int id) {
        this.teamPlayers = new HashSet<>();
        this.mole = null;
        this.king = null;

        this.alive = true;
        this.friendlyFire = false;

        this.owner = owner;
        this.playersAlive = 0;
        teamFlags = new ArrayList<>();

        this.name = String.format("%s team",owner.getName());

        this.inventory = Bukkit.createInventory(null, 27, Component.text(ChatUtils.format("&bInventario de " + name)));
        this.id = id;
        this.size = 0;

        if (Bukkit.getScoreboardManager().getMainScoreboard().getTeam(owner.getName()) == null) {
            this.team = Bukkit.getScoreboardManager().getMainScoreboard().registerNewTeam(owner.getName());

            team.prefix(Component.text(String.format("[Team %s] | ", owner.getName())));
            color = textColors[new Random().nextInt(textColors.length)];
            team.setColor(color);
            team.setAllowFriendlyFire(false);
        } else
            this.team = Bukkit.getScoreboardManager().getMainScoreboard().getTeam(owner.getName());

        addPlayer(owner);

        for(var flag : TeamFlags.values()) {
            if(flag.isDefaultEnabled()) {
                this.addFlag(flag);
            }
        }
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

    public void setFriendlyFire(boolean friendlyFire) {
        this.friendlyFire = friendlyFire;
        getTeam().setAllowFriendlyFire(friendlyFire);
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

    public void setColor(ChatColor color) {
        this.color = color;
    }

    public ChatColor getColor() {
        return color;
    }

    public boolean isFriendlyFire() {
        return friendlyFire;
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

    @Deprecated(forRemoval = true)
    public boolean isBlockChangeName() {
        return this.teamFlags.contains(TeamFlags.BLOCK_NAME_CHANGE);
    }

    public void sendMessage(String s) {
        getTeamPlayers().forEach(tP -> {
            var op = Bukkit.getOfflinePlayer(tP);

            if(op.isOnline()) {
                op.getPlayer().sendMessage(ChatUtils.TEAM + s);
            }
        });
    }

    public List<TeamFlags> getTeamFlags() {
        return teamFlags;
    }

    public boolean containsFlag(TeamFlags flag) {
        return this.teamFlags.contains(flag);
    }

    public void addFlag(TeamFlags flag) {
        if(!this.containsFlag(flag)) {
            this.teamFlags.add(flag);
            Bukkit.getPluginManager().callEvent(new TeamFlagChangedEvent(this, flag, true));
        }
    }

    public void removeFlag(TeamFlags flag) {
        if(this.teamFlags.contains(flag)) {
            this.teamFlags.remove(flag);
            Bukkit.getPluginManager().callEvent(new TeamFlagChangedEvent(this, flag, false));
        }
    }
}
