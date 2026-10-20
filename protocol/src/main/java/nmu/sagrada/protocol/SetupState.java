package nmu.sagrada.protocol;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
public final class SetupState extends Message {
    private static final long serialVersionUID = 2103L;
    public final String privateObjective;
    public final List<String> publicObjectives;
    public final List<String> windowChoices;
    public SetupState(String privateObjective, List<String> publicObjectives, List<String> windowChoices) {
        this.privateObjective = privateObjective;
        this.publicObjectives = Collections.unmodifiableList(new ArrayList<>(publicObjectives));
        this.windowChoices = Collections.unmodifiableList(new ArrayList<>(windowChoices));
    }
}
