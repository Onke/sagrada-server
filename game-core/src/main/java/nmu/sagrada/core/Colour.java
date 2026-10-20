package nmu.sagrada.core;

public enum Colour {
    BLUE, GREEN, PURPLE, RED, YELLOW;

    public static Colour fromPattern(char value) {
        switch (Character.toLowerCase(value)) {
            case 'b': return BLUE;
            case 'g': return GREEN;
            case 'p': return PURPLE;
            case 'r': return RED;
            case 'y': return YELLOW;
            default: return null;
        }
    }
}
