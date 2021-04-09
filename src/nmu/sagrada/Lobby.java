package nmu.sagrada;

import nmu.sagrada.messages.server.GameSetup;
import nmu.sagrada.messages.server.LobbyPlayersListed;
import nmu.sagrada.messages.server.StartGame;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Lobby {
    private static Map<Player, ClientHandler> clients = new HashMap<>();
    private static final int MAX_PLAYER_NUMBER = 4;
    private static int numPlayers = 0;
    private static ArrayList<String> lobby = new ArrayList<>();
    private static final ReentrantLock lock = new ReentrantLock();

    public static void addPlayer(String name, ClientHandler client) {
        lock.lock();
        Player newPlayer = new Player(name, numPlayers++);
        lobby.add(name);
        clients.put(newPlayer, client);

        if(clients.size() == MAX_PLAYER_NUMBER){
            for(Player player : clients.keySet())
                clients.get(player).send(new GameSetup());
            Game.players = clients;
            Game.setGame();
            //clients.clear();
        }
        else if(clients.size() < MAX_PLAYER_NUMBER) {
            for(Player player : clients.keySet())
                clients.get(player).send(new LobbyPlayersListed());
        }
lock.unlock();
    }

    public static ArrayList<String> getWaitingPlayers() {
        lock.lock();
        ArrayList<Player> players = new ArrayList<>(clients.keySet());
        ArrayList<String> playerNames = new ArrayList<>();
        for(Player player : players)
            playerNames.add(player.getName());
        lock.unlock();
        return playerNames;

    }


}
