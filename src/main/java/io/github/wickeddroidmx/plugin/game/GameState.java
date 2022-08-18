package io.github.wickeddroidmx.plugin.game;

public enum GameState {
    WAITING("Waiting"),
    PLAYING("Playing"),
    PVP("PVP"),
    MEETUP("Meetup"),
    FINISHING("Finishing");

    String format;

    GameState(String format) {
        this.format = format;
    }

    public String getFormat() {
        return format;
    }
}
