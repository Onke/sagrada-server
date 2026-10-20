package nmu.sagrada.protocol;
public final class ErrorMessage extends Message {
    private static final long serialVersionUID = 2107L;
    public final String message;
    public final boolean fatal;
    public ErrorMessage(String message, boolean fatal) { this.message = message; this.fatal = fatal; }
}
