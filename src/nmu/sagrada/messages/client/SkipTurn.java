package nmu.sagrada.messages.client;

import nmu.sagrada.ClientHandler;
import nmu.sagrada.Game;
import nmu.sagrada.messages.Message;

public class SkipTurn extends Message<ClientHandler>  {
    private static final long serialVersionUID = 104L;

    @Override
    public void apply(ClientHandler client) {
        Game.playerMove(client, -1, -1);
    }
}
