package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.objectives.Objective;

import java.util.ArrayList;

public abstract class PublicObjective implements Objective {


    public static final int UPSIDE_DOWN_NUMBER_PYRAMID = 1;
    public static final int THREE_FOUR_PAIR = 2;
    public static final int DIFFERENT_NUMBERS_COLUMN = 3;
    public static final int DIFFERENT_COLOURS_COLUMN = 4;
    public static final int FIVE_SIX_PAIR = 5;
    public static final int JAGGED_DIFFERENT_COLOURS = 6;
    public static final int DIFFERENT_COLOURS_ROW = 7;
    public static final int LIGHT_SHADES = 8;
    public static final int ONE_TWO = 9;
    public static final int DIFFERENT_NUMBERS_ROW = 10;


    public abstract int getNumber();


}