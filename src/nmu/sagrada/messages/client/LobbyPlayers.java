package nmu.sagrada.messages.client;

import nmu.sagrada.ClientHandler;
import nmu.sagrada.messages.Message;
import nmu.sagrada.messages.server.LobbyPlayersListed;

public class LobbyPlayers extends Message<ClientHandler> {
    private static final long serialVersionUID = 101L;

    @Override
    public String toString() {
        return "ListPlayers()";
    }

    @Override
    public void apply(ClientHandler client) {
        client.send(new LobbyPlayersListed());
    }
}
