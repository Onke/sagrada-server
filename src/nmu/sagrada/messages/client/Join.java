package nmu.sagrada.messages.client;

import nmu.sagrada.ClientHandler;
import nmu.sagrada.Lobby;
import nmu.sagrada.messages.Message;

public class Join extends Message<ClientHandler> {
    private static final long serialVersionUID = 100L;

    public String name;

    public Join(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return String.format("Join(%s)", name);
    }

    @Override
    public void apply(ClientHandler client) {
        Lobby.addPlayer(name, client);
    }
}
