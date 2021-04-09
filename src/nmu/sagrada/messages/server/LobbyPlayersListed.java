package nmu.sagrada.messages.server;

import nmu.sagrada.Lobby;
import nmu.sagrada.messages.Message;

import java.util.ArrayList;

public class LobbyPlayersListed extends Message {
    private static final long serialVersionUID = 205L;

    public ArrayList<String> playerNames;

    public LobbyPlayersListed() {
        playerNames = Lobby.getWaitingPlayers();
    }
}
