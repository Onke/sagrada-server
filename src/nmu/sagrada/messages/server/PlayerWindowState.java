package nmu.sagrada.messages.server;

import nmu.sagrada.messages.Message;

import java.util.ArrayList;

public class PlayerWindowState extends Message {
    private static final long serialVersionUID = 214L;
    public ArrayList<String> dicePositions;

    public PlayerWindowState(ArrayList<String> dicePositions) {
        this.dicePositions = dicePositions;
    }
}
