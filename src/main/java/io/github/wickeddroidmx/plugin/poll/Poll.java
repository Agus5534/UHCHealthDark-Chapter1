package io.github.wickeddroidmx.plugin.poll;

import io.github.wickeddroidmx.plugin.Main;
import org.bukkit.Bukkit;

import javax.inject.Inject;

public class Poll {
    @Inject
    private Main plugin;
    private final String ans1, ans2;

    private final String question;

    private int ans1Votes, ans2Votes, pollDuration;

    private boolean closed = false;


    public Poll(String question, String ans1, String ans2, int pollDuration) {
        this.question = question;
        this.ans1 = ans1;
        this.ans2 = ans2;
        this.pollDuration = pollDuration;

        ans1Votes = 0;
        ans2Votes = 0;

        Bukkit.getScheduler().runTaskLater(plugin, ()-> closed = true, 20*pollDuration);
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

    public void addVoteAns1() {
        ans1Votes++;
    }

    public void addVoteAns2() {
        ans2Votes++;
    }

    public int getAns1Votes() {
        return ans1Votes;
    }

    public int getAns2Votes() {
        return ans2Votes;
    }

    public int getPollDuration() {
        return pollDuration;
    }
}