package nmu.sagrada.protocol;

public final class Join extends Message {
    private static final long serialVersionUID = 2001L;
    public final String name;
    public final GameMode mode;
    public final int botCount;
    public final Difficulty difficulty;

    public Join(String name, GameMode mode, int botCount, Difficulty difficulty) {
        this.name = name;
        this.mode = mode;
        this.botCount = botCount;
        this.difficulty = difficulty;
    }
}
