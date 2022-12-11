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
    borderDelay,
    revealTime,
    timeWorldBorderOne,
    timeWorldBorderTwo,
    timeWorldBorderThree,
    sizeWorldBorderOne,
    sizeWorldBorderTwo,
    sizeWorldBorderThree;

    private long seconds;
    private GameState gameState;
    private int uhcId;
    private int cape;
    private int appleRate;
    private boolean pvpEnabled;
    private boolean spectators;
    private boolean arenaEnabled;
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
        this.spectators = true;
        this.arenaEnabled = true;

        this.teamWin = null;
        this.worldBorder = 2000;
        this.cape = 150;
        this.appleRate = 5;

        this.cobwebLimit = 16;
        this.seconds = 0;
        this.currentTime = 0;
        this.timeForPvP = 3600;
        this.timeForMeetup = 7200;
        this.revealTime = 5400;
        this.borderDelay = 300;

        this.timeWorldBorderOne = 7200;
        this.timeWorldBorderTwo = 7500;
        this.timeWorldBorderThree = 7800;
        this.sizeWorldBorderOne = 150;
        this.sizeWorldBorderTwo = 70;
        this.sizeWorldBorderThree = 30;
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

    public void setRevealTime(int revealTime) {
        this.revealTime = revealTime;
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

    public void setAppleRate(int appleRate) {
        this.appleRate = appleRate;
    }

    public boolean isSpectators() {
        return spectators;
    }

    public void setSpectators(boolean spectators) {
        this.spectators = spectators;
    }

    public boolean isArenaEnabled() {
        return arenaEnabled;
    }

    public void setArenaEnabled(boolean arenaEnabled) {
        this.arenaEnabled = arenaEnabled;
    }

    public void setSpectatorTeam(Team spectatorTeam) {
        this.spectatorTeam = spectatorTeam;
    }

    public void setPvpEnabled(boolean pvpEnabled) {
        this.pvpEnabled = pvpEnabled;
    }

    public void setTimeForMeetup(int timeForMeetup) {
        this.timeForMeetup = timeForMeetup;
        setTimeWorldBorderOne(timeForMeetup);
        setTimeWorldBorderTwo(getTimeWorldBorderOne()+300);
        setTimeWorldBorderThree(getTimeWorldBorderTwo()+300);
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

    public int getRevealTime() {
        return revealTime;
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

    public int getAppleRate() {
        return appleRate;
    }

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

    public int getTimeWorldBorderOne() {
        return timeWorldBorderOne;
    }

    public int getTimeWorldBorderThree() {
        return timeWorldBorderThree;
    }

    public int getTimeWorldBorderTwo() {
        return timeWorldBorderTwo;
    }

    public void setTimeWorldBorderOne(int timeWorldBorderOne) {
        this.timeWorldBorderOne = timeWorldBorderOne;
    }

    public void setTimeWorldBorderThree(int timeWorldBorderThree) {
        this.timeWorldBorderThree = timeWorldBorderThree;
    }

    public void setTimeWorldBorderTwo(int timeWorldBorderTwo) {
        this.timeWorldBorderTwo = timeWorldBorderTwo;
    }

    public int getSizeWorldBorderOne() {
        return sizeWorldBorderOne;
    }

    public int getSizeWorldBorderThree() {
        return sizeWorldBorderThree;
    }

    public int getSizeWorldBorderTwo() {
        return sizeWorldBorderTwo;
    }

    public void setSizeWorldBorderOne(int sizeWorldBorderOne) {
        this.sizeWorldBorderOne = sizeWorldBorderOne;
    }

    public void setSizeWorldBorderThree(int sizeWorldBorderThree) {
        this.sizeWorldBorderThree = sizeWorldBorderThree;
    }

    public void setSizeWorldBorderTwo(int sizeWorldBorderTwo) {
        this.sizeWorldBorderTwo = sizeWorldBorderTwo;
    }
}
