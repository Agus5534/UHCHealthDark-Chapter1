package io.github.wickeddroidmx.plugin.poll;

import io.github.wickeddroidmx.plugin.utils.bossbar.BossBarCreator;
import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;
import net.kyori.adventure.bossbar.BossBar;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class PollManager {

    private Poll activePoll;

    private BossBarCreator bossBarCreator;

    public PollManager() {
        activePoll = null;
    }

    public void setPoll(Poll poll) {
        activePoll = poll;
    }

    public void closeActivePoll() {
        activePoll.setClosed(true);
    }

    public Poll getActivePoll() {
        return activePoll;
    }

    public enum Winner {
        ANSWER1,
        ANSWER2,
        DRAW,
        NOBODY_YET
    }

    public Winner getWinner() {
        return (!activePoll.isClosed() ? Winner.NOBODY_YET : (activePoll.getAns1Votes() == activePoll.getAns2Votes() ? Winner.DRAW : (activePoll.getAns1Votes() > activePoll.getAns2Votes() ? Winner.ANSWER1 : Winner.ANSWER2)));
    }

    public void updatePollStatus(JavaPlugin plugin) {
        if(this.activePoll == null) { return; }

        if(this.activePoll.isClosed()) {
            Bukkit.getOnlinePlayers().forEach(p -> bossBarCreator.hideBossBar(p));
            announceWinner();
            this.activePoll = null;
            return;
        }

        if(bossBarCreator == null) {
            bossBarCreator = new BossBarCreator(
                    plugin,
                    ChatColor.GOLD + this.activePoll.getQuestion(),
                    BossBar.Color.GREEN,
                    BossBar.Overlay.NOTCHED_20,
                    1.0F
            );
        }

        bossBarCreator.setName(ChatUtils.formatC(ChatColor.GOLD + this.activePoll.getQuestion()));
        Bukkit.getOnlinePlayers().forEach(p -> bossBarCreator.showBossBar(p));

        showActionBar();

        long seconds = Math.negateExact(TimeUnit.MILLISECONDS.toSeconds(activePoll.getStartMillis()-System.currentTimeMillis()));

        float progress = 1.0F- ((float)seconds/activePoll.getPollDuration());

        if(progress < 0.0F) { progress = 0; }

        bossBarCreator.setProgress(progress);
    }

    private void announceWinner() {
        Component message;
        if(getWinnerAnswer().equalsIgnoreCase("Empate")) {
            message = ChatUtils.formatC(ChatUtils.PREFIX + String.format("¡La encuesta ha quedado en un &6%s&7! \n" +
                    "'&6%s&7' quedó con &6%d &7votos. \n" +
                    "'&6%s&7' quedó con &6%d &7votos.",
                    getWinnerAnswer(),
                    activePoll.getAns1(),
                    activePoll.getAns1Votes(),
                    activePoll.getAns2(),
                    activePoll.getAns2Votes()));
        } else {
            message = ChatUtils.formatC(ChatUtils.PREFIX + String.format("¡La encuesta ha finalizado! \n" +
                            "Ha resultado ganadora la opción '&6%s&7' con &6%d &7votos. \n" +
                            "La otra opción ha quedado con &6%d &7votos.",
                    getWinnerAnswer(),
                    votesWinner(),
                    votesLoser()));
        }


        Bukkit.broadcast(message);
    }

    private void showActionBar() {
        if(activePoll == null) { return; }

        if(activePoll.isClosed()) { return; }

        Bukkit.getOnlinePlayers().forEach(p -> {
            p.sendActionBar(ChatUtils.formatC("&6Vota en la poll activa con &b/pollvote"));
        });
    }

    private String getWinnerAnswer() {
        switch (getWinner()) {
            case DRAW -> { return "Empate"; }
            case ANSWER1 -> { return this.activePoll.getAns1(); }
            case ANSWER2 -> { return this.activePoll.getAns2(); }
            default -> { return "Desconocido"; }
        }
    }

    private int votesWinner() {
        switch (getWinner()) {
            case DRAW -> { return this.activePoll.getAns1Votes(); }
            case ANSWER1 -> { return this.activePoll.getAns1Votes(); }
            case ANSWER2 -> { return this.activePoll.getAns2Votes(); }
            default -> { return -1; }
        }
    }

    private int votesLoser() {
        switch (getWinner()) {
            case DRAW -> { return this.activePoll.getAns1Votes(); }
            case ANSWER1 -> { return this.activePoll.getAns2Votes(); }
            case ANSWER2 -> { return this.activePoll.getAns1Votes(); }
            default -> { return -1; }
        }
    }
}
