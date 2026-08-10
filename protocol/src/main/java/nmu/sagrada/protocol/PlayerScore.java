package nmu.sagrada.protocol;

import java.io.Serializable;

public final class PlayerScore implements Serializable {
    private static final long serialVersionUID = 2203L;
    public final String playerId;
    public final String name;
    public final boolean bot;
    public final int total;
    public final int publicScore;
    public final int privateScore;
    public final int emptyPenalty;
    /** Revealed only once the match is over — the colour this player was secretly collecting. */
    public final String privateObjective;

    public PlayerScore(String playerId, String name, boolean bot, int total, int publicScore,
                       int privateScore, int emptyPenalty, String privateObjective) {
        this.playerId = playerId;
        this.name = name;
        this.bot = bot;
        this.total = total;
        this.publicScore = publicScore;
        this.privateScore = privateScore;
        this.emptyPenalty = emptyPenalty;
        this.privateObjective = privateObjective;
    }
}
