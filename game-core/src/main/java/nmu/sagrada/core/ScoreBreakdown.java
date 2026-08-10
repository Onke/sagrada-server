package nmu.sagrada.core;

public final class ScoreBreakdown {
    private final int publicScore;
    private final int privateScore;
    private final int emptyPenalty;

    public ScoreBreakdown(int publicScore, int privateScore, int emptyPenalty) {
        this.publicScore = publicScore;
        this.privateScore = privateScore;
        this.emptyPenalty = emptyPenalty;
    }

    public int getPublicScore() { return publicScore; }
    public int getPrivateScore() { return privateScore; }
    public int getEmptyPenalty() { return emptyPenalty; }
    public int getTotal() { return publicScore + privateScore - emptyPenalty; }
}
