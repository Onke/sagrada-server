package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.Colour;
import nmu.sagrada.Die;

import java.util.HashMap;
import java.util.Map;

public class Dice {

    Map<Integer, Die> blue = new HashMap<>();
    Map<Integer, Die> green = new HashMap<>();
    Map<Integer, Die> purple = new HashMap<>();
    Map<Integer, Die> yellow = new HashMap<>();
    Map<Integer, Die> red = new HashMap<>();

    public Dice() {
        initialise();
    }

    public void initialise(){
        blue.put(1,new Die(Colour.BLUE, 1));
        blue.put(2,new Die(Colour.BLUE, 2));
        blue.put(3,new Die(Colour.BLUE, 3));
        blue.put(4,new Die(Colour.BLUE, 4));
        blue.put(5,new Die(Colour.BLUE, 5));
        blue.put(6,new Die(Colour.BLUE, 6));

        green.put(1,new Die(Colour.GREEN, 1));
        green.put(2,new Die(Colour.GREEN, 2));
        green.put(3,new Die(Colour.GREEN, 3));
        green.put(4,new Die(Colour.GREEN, 4));
        green.put(5,new Die(Colour.GREEN, 5));
        green.put(6,new Die(Colour.GREEN, 6));

        purple.put(1,new Die(Colour.PURPLE, 1));
        purple.put(2,new Die(Colour.PURPLE, 2));
        purple.put(3,new Die(Colour.PURPLE, 3));
        purple.put(4,new Die(Colour.PURPLE, 4));
        purple.put(5,new Die(Colour.PURPLE, 5));
        purple.put(6,new Die(Colour.PURPLE, 6));

        yellow.put(1,new Die(Colour.YELLOW, 1));
        yellow.put(2,new Die(Colour.YELLOW, 2));
        yellow.put(3,new Die(Colour.YELLOW, 3));
        yellow.put(4,new Die(Colour.YELLOW, 4));
        yellow.put(5,new Die(Colour.YELLOW, 5));
        yellow.put(6,new Die(Colour.YELLOW, 6));

        red.put(1,new Die(Colour.RED, 1));
        red.put(2,new Die(Colour.RED, 2));
        red.put(3,new Die(Colour.RED, 3));
        red.put(4,new Die(Colour.RED, 4));
        red.put(5,new Die(Colour.RED, 5));
        red.put(6,new Die(Colour.RED, 6));


    }

}
