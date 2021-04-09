package nmu.sagrada.board;

import nmu.sagrada.Die;

public class WindowCardBox extends Box {
    private String boxID;
    private char colour;
    private int value;

    public WindowCardBox(String boxID) {
        this.boxID = boxID;
        this.colour = ' ';
        this.value = 0;
    }

    public WindowCardBox(String boxID, char colour){
        this.boxID = boxID;
        this.colour = colour;
        this.value = 0;
    }
    public WindowCardBox(String boxID, int value){
        this.boxID = boxID;
        this.colour = ' ';
        this.value = value;
    }


    //Getters and Setters
    public String getBoxID() {
        return boxID;
    }

    public void setBoxID(String boxID) {
        this.boxID = boxID;
    }

    public char getColour() {
        return colour;
    }

    public void setColour(char colour) {
        this.colour = colour;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
