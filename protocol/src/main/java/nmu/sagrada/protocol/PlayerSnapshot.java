package nmu.sagrada.protocol;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class PlayerSnapshot implements Serializable {
    private static final long serialVersionUID = 2202L;
    public final String id;
    public final String name;
    public final boolean bot;
    public final String windowId;
    public final List<String> dicePositions;
    public PlayerSnapshot(String id, String name, boolean bot, String windowId, List<String> dicePositions) {
        this.id = id;
        this.name = name;
        this.bot = bot;
        this.windowId = windowId;
        this.dicePositions = Collections.unmodifiableList(new ArrayList<>(dicePositions));
    }
}
