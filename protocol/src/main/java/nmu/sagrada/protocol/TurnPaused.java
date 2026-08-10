package nmu.sagrada.protocol;

/**
 * A bot has finished its turn and the server is waiting rather than advancing on a timer, so the
 * human can look at what it did. Answer with {@link ContinueTurn}.
 */
public final class TurnPaused extends Message {
    private static final long serialVersionUID = 2108L;
    public final String playerId;
    public final boolean placed;

    public TurnPaused(String playerId, boolean placed) {
        this.playerId = playerId;
        this.placed = placed;
    }
}
