package io.github.wickeddroidmx.plugin.game;

import io.github.wickeddroidmx.plugin.player.DeathType;
import io.github.wickeddroidmx.plugin.teams.UhcTeam;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Team;

import javax.inject.Singleton;

@Singleton
public class GameManager {

    private Team spectatorTeam;

    private Player host;
    private UhcTeam teamWin;
    private DeathType deathType;
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
    sizeWorldBorderThree,
    tiSize;

    private long seconds;
    private GameState gameState;
    private String uhcId;
    private int maxPlayerSize;
    private int cape;
    private int appleRate;
    private boolean pvpEnabled;
    private boolean spectators;
    private boolean arenaEnabled;
    private boolean gameStart;
    private boolean runMode;
    private boolean belowNameHealth;
    private boolean scenarioLimit;
    private boolean scatteredPlayers;

    public GameManager() {
        this.host = null;
        this.uhcId = "253";

        this.gameState = GameState.WAITING;
        this.scatteredPlayers = false;

        this.gameStart = false;
        this.runMode = false;
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
        this.maxPlayerSize = Bukkit.getServer().getMaxPlayers();

        this.tiSize = 27;
        this.deathType = DeathType.NORMAL;
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

    public void setCurrentTime(int currentTime) {
        this.currentTime = currentTime;
    }

    public int getTiSize() {
        return tiSize;
    }

    public void setTiSize(int tiSize) {
        this.tiSize = tiSize;
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

    public void setUhcId(String uhcId) {
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

    public String getUhcId() {
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

    public DeathType getDeathType() {
        return deathType;
    }

    public void setDeathType(DeathType deathType) {
        this.deathType = deathType;
    }

    public int getMaxPlayerSize() {
        return maxPlayerSize;
    }

    public void setMaxPlayerSize(int maxPlayerSize) {
        this.maxPlayerSize = maxPlayerSize;
    }

    public boolean isBelowNameHealth() {
        return belowNameHealth;
    }

    public void setBelowNameHealth(boolean belowNameHealth) {
        this.belowNameHealth = belowNameHealth;
    }
}
