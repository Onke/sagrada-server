package nmu.sagrada.protocol;
import java.io.Serializable;
public final class PlayerSummary implements Serializable {
    private static final long serialVersionUID = 2201L;
    public final String id;
    public final String name;
    public final boolean bot;
    public PlayerSummary(String id, String name, boolean bot) { this.id = id; this.name = name; this.bot = bot; }
}
