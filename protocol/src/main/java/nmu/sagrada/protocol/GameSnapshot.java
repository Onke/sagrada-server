package nmu.sagrada.protocol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class GameSnapshot extends Message {
    private static final long serialVersionUID = 2104L;
    public final int round;
    public final String activePlayerId;
    public final List<String> draftPool;
    public final List<PlayerSnapshot> players;
    public final long revision;
    public GameSnapshot(int round, String activePlayerId, List<String> draftPool, List<PlayerSnapshot> players, long revision) {
        this.round = round;
        this.activePlayerId = activePlayerId;
        this.draftPool = Collections.unmodifiableList(new ArrayList<>(draftPool));
        this.players = Collections.unmodifiableList(new ArrayList<>(players));
        this.revision = revision;
    }
}
