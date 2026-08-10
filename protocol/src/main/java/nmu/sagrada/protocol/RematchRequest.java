package nmu.sagrada.protocol;

public final class RematchRequest extends Message {
    private static final long serialVersionUID = 2006L;
    public final int botCount;
    public final Difficulty difficulty;

    public RematchRequest(int botCount, Difficulty difficulty) {
        this.botCount = botCount;
        this.difficulty = difficulty;
    }
}
