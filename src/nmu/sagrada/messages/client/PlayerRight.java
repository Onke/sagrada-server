package nmu.sagrada.messages.client;

import nmu.sagrada.ClientHandler;
import nmu.sagrada.messages.Message;

public class PlayerRight extends Message<ClientHandler> {
    private static final long serialVersionUID = 109L;

    public String name;

    public PlayerRight(String name) {
        this.name = name;
    }
}
