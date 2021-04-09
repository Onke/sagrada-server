package nmu.sagrada.messages.server;

import nmu.sagrada.Die;
import nmu.sagrada.messages.Message;

import java.util.ArrayList;

public class DraftPool extends Message {
    private static final long serialVersionUID = 200L;

    public ArrayList<String> draftPool;
    public DraftPool(ArrayList<String> dratPool) {
        this.draftPool = dratPool;
    }
}
