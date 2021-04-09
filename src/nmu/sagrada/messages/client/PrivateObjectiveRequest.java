package nmu.sagrada.messages.client;

import nmu.sagrada.ClientHandler;
import nmu.sagrada.Game;
import nmu.sagrada.messages.Message;

public class PrivateObjectiveRequest extends Message<ClientHandler> {
    private static final long serialVersionUID = 110L;

    @Override
    public void apply(ClientHandler client) {
        Game.getPrivateObjective(client);
    }
}
