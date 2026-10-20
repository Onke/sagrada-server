package nmu.sagrada.protocol;
public final class PlaceDie extends Message {
    private static final long serialVersionUID = 2003L;
    public final int draftIndex;
    public final int cellIndex;
    public PlaceDie(int draftIndex, int cellIndex) { this.draftIndex = draftIndex; this.cellIndex = cellIndex; }
}
