package io.github.wickeddroidmx.plugin.poll;

import io.github.wickeddroidmx.plugin.utils.chat.ChatUtils;

import javax.inject.Singleton;

@Singleton
public class PollManager {

    private Poll activePoll;

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
        if(!activePoll.isClosed()) {
            return Winner.NOBODY_YET;
        }

        if(activePoll.getAns1Votes() == activePoll.getAns2Votes()) {
            return Winner.DRAW;
        }

        if(activePoll.getAns1Votes() > activePoll.getAns2Votes()) {
            return Winner.ANSWER1;
        }

        if(activePoll.getAns2Votes() > activePoll.getAns1Votes()) {
            return Winner.ANSWER2;
        }

        return Winner.NOBODY_YET;
    }
}
