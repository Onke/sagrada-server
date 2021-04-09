package nmu.sagrada.objectives.privateObjective;

import nmu.sagrada.Colour;
import nmu.sagrada.board.WindowCard;
import nmu.sagrada.objectives.Objective;

public class PrivateObjective implements Objective {

    private Colour colour;

    public PrivateObjective(Colour colour) {
        super();
        this.colour = colour;
    }

    //Getters and setters
    public Colour getColour() {
        return colour;
    }

    public void setColour(Colour colour) {
        this.colour = colour;
    }


    @Override
    public int calculatePoints(WindowCard windowCard) {
        return 0;
    }
}