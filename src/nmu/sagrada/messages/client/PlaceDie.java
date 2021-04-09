package nmu.sagrada.messages.client;

import nmu.sagrada.ClientHandler;
import nmu.sagrada.Game;
import nmu.sagrada.messages.Message;

public class PlaceDie extends Message<ClientHandler>  {
    private static final long serialVersionUID = 102L;
    private  int draftPoolSelection, windowSelection;

    public PlaceDie(int draftPoolSelection, int windowSelection) {
        this.draftPoolSelection = draftPoolSelection;
        this.windowSelection = windowSelection;

    }

    @Override
    public void apply(ClientHandler client) {
        Game.playerMove(client, draftPoolSelection, windowSelection);
    }
}
