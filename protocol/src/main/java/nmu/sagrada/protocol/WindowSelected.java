package nmu.sagrada.protocol;
public final class WindowSelected extends Message {
    private static final long serialVersionUID = 2005L;
    public final String windowId;
    public WindowSelected(String windowId) { this.windowId = windowId; }
}
