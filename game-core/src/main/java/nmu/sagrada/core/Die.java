package nmu.sagrada.core;

import java.util.Objects;

public final class Die {
    private final Colour colour;
    private final int value;

    public Die(Colour colour, int value) {
        if (colour == null) throw new IllegalArgumentException("colour is required");
        if (value < 1 || value > 6) throw new IllegalArgumentException("value must be between 1 and 6");
        this.colour = colour;
        this.value = value;
    }

    public Colour getColour() { return colour; }
    public int getValue() { return value; }
    public String getId() { return colour.name() + value; }

    public static Die fromId(String id) {
        if (id == null || id.length() < 2) throw new IllegalArgumentException("Invalid die id");
        return new Die(Colour.valueOf(id.substring(0, id.length() - 1)),
                Integer.parseInt(id.substring(id.length() - 1)));
    }

    @Override public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Die)) return false;
        Die die = (Die) other;
        return value == die.value && colour == die.colour;
    }

    @Override public int hashCode() { return Objects.hash(colour, value); }
    @Override public String toString() { return getId(); }
}
