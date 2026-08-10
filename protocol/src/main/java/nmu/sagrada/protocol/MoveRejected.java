package nmu.sagrada.protocol;
public final class MoveRejected extends Message {
    private static final long serialVersionUID = 2105L;
    public final String code;
    public final String message;
    public final GameSnapshot snapshot;
    public MoveRejected(String code, String message, GameSnapshot snapshot) {
        this.code = code; this.message = message; this.snapshot = snapshot;
    }
}
