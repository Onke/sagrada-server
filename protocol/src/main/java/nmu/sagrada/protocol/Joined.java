package nmu.sagrada.protocol;
public final class Joined extends Message {
    private static final long serialVersionUID = 2101L;
    public final String playerId;
    public final boolean host;
    public Joined(String playerId, boolean host) { this.playerId = playerId; this.host = host; }
}
