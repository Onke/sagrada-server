package nmu.sagrada.protocol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class EndGame extends Message {
    private static final long serialVersionUID = 2106L;
    public final List<PlayerScore> standings;
    public EndGame(List<PlayerScore> standings) { this.standings = Collections.unmodifiableList(new ArrayList<>(standings)); }
}
