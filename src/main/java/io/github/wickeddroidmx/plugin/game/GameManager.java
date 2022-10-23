package io.github.wickeddroidmx.plugin.game;

import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import io.github.wickeddroidmx.plugin.utils.fastboard.FastBoard;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.inject.Singleton;

@Singleton
public class GameManager {

    private Team spectatorTeam;

    private Player host;
    private UhcTeam teamWin;

    private int currentTime,
    timeForPvP,
    timeForMeetup,
    worldBorder,
    cobwebLimit,
    borderDelay;

    private long seconds;
    private GameState gameState;
    private int uhcId;
    private int cape;
    private boolean pvpEnabled;
    private boolean gameStart;
    private boolean runMode;

    private boolean skyHighMode;
    private boolean scenarioLimit;
    private boolean scatteredPlayers;

    public GameManager() {
        this.host = null;
        this.uhcId = 253;

        this.gameState = GameState.WAITING;
        this.scatteredPlayers = false;

        this.gameStart = false;
        this.runMode = false;
        this.skyHighMode = false;
        this.scenarioLimit = true;

        this.teamWin = null;
        this.worldBorder = 2000;
        this.cape = 150;

        this.cobwebLimit = 12;
        this.seconds = 0;
        this.currentTime = 0;
        this.timeForPvP = 3600;
        this.timeForMeetup = 7200;
        this.borderDelay = 300;
    }

    public void setHost(Player host) {
        this.host = host;
    }

    public void setCobwebLimit(int cobwebLimit) {
        this.cobwebLimit = cobwebLimit;
    }

    public void setTeamWin(UhcTeam teamWin) {
        this.teamWin = teamWin;
    }

    public void setSeconds(long seconds) {
        this.seconds = seconds;
    }

    public void setScenarioLimit(boolean scenarioLimit) {
        this.scenarioLimit = scenarioLimit;
    }

    public void setRunMode(boolean runMode) {
        this.runMode = runMode;
    }

    public void setSkyHighMode(boolean skyHighMode) {
        this.skyHighMode = skyHighMode;
    }

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public void setSpectatorTeam(Team spectatorTeam) {
        this.spectatorTeam = spectatorTeam;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    public void setTimeForMeetup(int timeForMeetup) {
        this.timeForMeetup = timeForMeetup;
    }

    public void setScatteredPlayers(boolean scatteredPlayers) {
        this.scatteredPlayers = scatteredPlayers;
    }

    public void setTimeForPvP(int timeForPvP) {
        this.timeForPvP = timeForPvP;
    }

    public void setUhcId(int uhcId) {
        this.uhcId = uhcId;
    }

    public void setGameStart(boolean gameStart) {
        this.gameStart = gameStart;
    }

    public void setWorldBorder(int worldBorder) {
        this.worldBorder = worldBorder;
    }

    public void setCape(int cape) {
        this.cape = cape;
    }

    public Team getSpectatorTeam() {
        return spectatorTeam;
    }

    public GameState getGameState() {
        return gameState;
    }

    public Player getHost() {
        return host;
    }

    public int getUhcId() {
        return uhcId;
    }

    public int getCurrentTime() {
        return currentTime;
    }

    public int getTimeForMeetup() {
        return timeForMeetup;
    }

    public int getBorderDelay() {
        return borderDelay;
    }

    public void setBorderDelay(int borderDelay) {
        this.borderDelay = borderDelay;
    }

    public UhcTeam getTeamWin() {
        return teamWin;
    }

    public int getWorldBorder() {
        return worldBorder;
    }

    public int getTimeForPvP() {
        return timeForPvP;
    }

    public long getSeconds() {
        return seconds;
    }

    public int getCape() { return cape; }

    public int getCobwebLimit() {
        return cobwebLimit;
    }

    public boolean isScenarioLimit() {
        return scenarioLimit;
    }

    public boolean isRunMode() {
        return runMode;
    }

    public boolean isSkyHighMode() {
        return skyHighMode;
    }

    public boolean isPvpEnabled() {
        return pvpEnabled;
    }

    public boolean isScatteredPlayers() {
        return scatteredPlayers;
    }

    public boolean isGameStart() {
        return gameStart;
    }

    private void registerTeams() {

    }
}
