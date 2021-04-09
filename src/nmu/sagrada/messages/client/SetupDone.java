package nmu.sagrada.messages.client;

import nmu.sagrada.ClientHandler;
import nmu.sagrada.Game;
import nmu.sagrada.Lobby;
import nmu.sagrada.messages.Message;

public class SetupDone extends Message<ClientHandler> {
    private static final long serialVersionUID = 107L;

    @Override
    public void apply(ClientHandler client) {
        Game.setupDone(client);
    }
}
