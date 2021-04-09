package nmu.sagrada;

import nmu.sagrada.board.WindowCard;
import nmu.sagrada.objectives.privateObjective.PrivateObjective;

import java.util.ArrayList;

public class Player {
    private String name;

    private int id;
    private int points;
    private PrivateObjective privateObjective;
    private WindowCard windowCard;
    private ArrayList<String> windowChoices;
    private boolean setupDone;
    private boolean active;
    private boolean skipTurn;


    public Player(String name, int id) {
        this.name = name;
        this.id = id;
        setupDone = false;
        active = false;
        skipTurn = false;

    }




    //Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public PrivateObjective getPrivateObjective() {
        return privateObjective;
    }

    public void setPrivateObjective(PrivateObjective privateObjective) {
        this.privateObjective = privateObjective;
    }

    public WindowCard getWindowCard() {
        return windowCard;
    }

    public void setWindowCard(WindowCard windowCard) {
        this.windowCard = windowCard;
    }

    public boolean isSetupDone() {
        return setupDone;
    }

    public void setSetupDone(boolean setupDone) {
        this.setupDone = setupDone;
    }
    public ArrayList<String> getWindowChoices() {
        return windowChoices;
    }

    public void setWindowChoices(ArrayList<String> windowChoices) {
        this.windowChoices = windowChoices;
    }
    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSkipTurn() {
        return skipTurn;
    }

    public void setSkipTurn(boolean skipTurn) {
        this.skipTurn = skipTurn;
    }
}