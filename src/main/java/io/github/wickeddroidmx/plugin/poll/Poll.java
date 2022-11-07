package io.github.wickeddroidmx.plugin.poll;

import io.github.wickeddroidmx.plugin.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitTask;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

public class Poll {
    @Inject
    private Main plugin;
    private final String ans1, ans2;

    private final String question;

    private int ans1Votes, ans2Votes, pollDuration;

    private boolean closed = false;

    private final ConcursantTypes concursantTypes;

    private final BukkitTask bukkitTask;

    private List<Player> concursants;


    public Poll(Main plugin, String question, String ans1, String ans2, int pollDuration, ConcursantTypes concursantTypes) {
        this.plugin = plugin;
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.pollDuration = pollDuration;
        this.concursantTypes = concursantTypes;

        ans1Votes = 0;
        ans2Votes = 0;
        concursants = new ArrayList<>();

        bukkitTask = Bukkit.getScheduler().runTaskLater(plugin, ()-> closed = true, 20*pollDuration);
    }


    public String getAns1() {
        return ans1;
    }

    public String getAns2() {
        return ans2;
    }

    public boolean isClosed() {
        return closed;
    }

    public void setClosed(boolean closed) {
        this.closed = closed;
    }

    public String getQuestion() {
        return question;
    }

    private void addVoteAns1() {
        ans1Votes++;
    }

    public void registerVote(Player player, int answerNumber) {
        if(answerNumber == 1) {
            addVoteAns1();
        } else { addVoteAns2(); }

        addConcursant(player);
    }

    private void addVoteAns2() {
        ans2Votes++;
    }

    public int getAns1Votes() {
        return ans1Votes;
    }

    public int getAns2Votes() {
        return ans2Votes;
    }

    public List<Player> getConcursants() {
        return concursants;
    }

    public int getPollDuration() {
        return pollDuration;
    }

    public ConcursantTypes getConcursantTypes() {
        return concursantTypes;
    }

    public void addConcursant(Player player) {
        this.concursants.add(player);
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }
}
