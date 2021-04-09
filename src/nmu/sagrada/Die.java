package nmu.sagrada;

public class Die {
    private Colour colour;
    private int value;

    public Die(Colour colour, int value) {
        this.colour = colour;
        this.value = value;
    }
    public void setDie(Die die){
        this.colour = die.getColour();
        this.value = die.getValue();
    }

    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
