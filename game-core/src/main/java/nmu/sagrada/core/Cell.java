package nmu.sagrada.core;

public final class Cell {
    private final Colour colourRestriction;
    private final int valueRestriction;
    private Die die;

    Cell(char patternValue) {
        colourRestriction = Colour.fromPattern(patternValue);
        valueRestriction = Character.isDigit(patternValue) && patternValue != '0'
                ? Character.digit(patternValue, 10) : 0;
    }

    public Colour getColourRestriction() { return colourRestriction; }
    public int getValueRestriction() { return valueRestriction; }
    public Die getDie() { return die; }
    public boolean isEmpty() { return die == null; }
    void place(Die value) { die = value; }
}
