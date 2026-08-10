package nmu.sagrada.protocol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class LobbyState extends Message {
    private static final long serialVersionUID = 2102L;
    public final GameMode mode;
    public final List<PlayerSummary> players;
    public final int requiredHumanPlayers;
    public LobbyState(GameMode mode, List<PlayerSummary> players, int requiredHumanPlayers) {
        this.mode = mode;
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.requiredHumanPlayers = requiredHumanPlayers;
    }
}
