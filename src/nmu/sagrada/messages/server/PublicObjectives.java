package nmu.sagrada.messages.server;

import nmu.sagrada.messages.Message;
import nmu.sagrada.objectives.publicObjectives.PublicObjective;

import java.util.ArrayList;

public class PublicObjectives extends Message {
    private static final long serialVersionUID = 208L;

    public ArrayList<String> publicObjectives;

    public PublicObjectives(ArrayList<String> publicObjectives) {
        this.publicObjectives = publicObjectives;
    }
}
