package nmu.sagrada.messages.server;

import nmu.sagrada.messages.Message;

import java.util.ArrayList;

public class WindowList extends Message {
    private static final long serialVersionUID = 210L;

    public ArrayList<String> windows;

    public WindowList(ArrayList<String> windows) {
        this.windows = windows;
    }
}
