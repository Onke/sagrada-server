package nmu.sagrada;

import java.util.*;

public class RoundManager {

    private ArrayList<Player> players;
    private Queue<Player> order;
    private Queue<Player> reverseOrder;
    private Boolean isFirstRound;

    public RoundManager(ArrayList<Player> players) {
        this.players = players;
        order = new LinkedList<>();
        reverseOrder = new LinkedList<>();
        isFirstRound = true;
    }

    /**
     * Gets the player order to start the first round
     *
     **/
    private void startOrder() {
        Collections.shuffle(players);
        order.addAll(players);
        for(int i = players.size() - 1; i >= 0; i--)
            reverseOrder.add(players.get(i));
    }

    /**
     * Sets the order of the new round to an ant-clockwise dirrection
     *
     */
    public void playerOrder() {
        if(isFirstRound){
            startOrder();
            isFirstRound = false;
            return;
        }
        ArrayList<Player> newRoundOrder = new ArrayList<>();
        newRoundOrder.set(0, players.get(3));
        for (int i = 1; i < players.size(); i++)
            newRoundOrder.set(i, players.get(i - 1));
        players = newRoundOrder;
        order.addAll(players);
        for(int i = players.size() - 1; i >= 0; i--)
            reverseOrder.add(players.get(i));

    }

    public Player getNextPlayer(){
        if(!order.isEmpty()){
            return order.remove();
        }
        else if(!reverseOrder.isEmpty()){
            return reverseOrder.remove();
        }
        else
            return null;
    }
}
