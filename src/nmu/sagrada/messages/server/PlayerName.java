package nmu.sagrada.messages.server;

import nmu.sagrada.messages.Message;

public class PlayerName extends Message {
    private static final long serialVersionUID = 204L;

    public String name;

    public PlayerName(String name) {
        this.name = name;
    }
}
