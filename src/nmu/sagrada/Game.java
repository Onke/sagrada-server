package nmu.sagrada;

import nmu.sagrada.board.Box;
import nmu.sagrada.board.WindowCard;
import nmu.sagrada.board.WindowCardBox;
import nmu.sagrada.messages.Message;
import nmu.sagrada.messages.server.*;
import nmu.sagrada.objectives.privateObjective.PrivateObjective;
import nmu.sagrada.objectives.publicObjectives.*;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

public class Game {

    public static Map<Player, ClientHandler> players;
    private static Map<Player, Integer> scores;
    private static int round;
    private static ArrayList<Die> gameDice;
    private static ArrayList<PublicObjective> publicObjectives;
    private static ArrayList<Die> dratPool;
    private static boolean isFirstRound;
    private static RoundManager roundManager;
    private static Player activePlayer;

    private static final Map<String, Colour> colours = new HashMap<String, Colour>() {
        {
            put("BLUE", Colour.BLUE);
            put("YELLOW", Colour.YELLOW);
            put("RED", Colour.RED);
            put("GREEN", Colour.GREEN);
            put("PURPLE", Colour.PURPLE);
        }
    };

    private static final Map<String, PublicObjective> publicObjectiveList = new HashMap<String, PublicObjective>() {
        {
            put("UPSIDE_DOWN_NUMBER_PYRAMID", new UpsideDownNumberPyramid());
            put("THREE_FOUR_PAIR", new ThreeFourPair());
            put("DIFFERENT_NUMBERS_COLUMN", new DifferentNumbersColumn());
            put("DIFFERENT_COLOURS_COLUMN", new DifferentColoursColumn());
            put("FIVE_SIX_PAIR", new FiveSixPair());
            put("JAGGED_DIFFERENT_COLOURS ", new JaggedDifferentColours());
            put("DIFFERENT_COLOURS_ROW", new DifferentColoursRow());
            put("LIGHT_SHADES", new BlueYellowPattern());
            put("ONE_TWO", new OneTwoPair());
            put("DIFFERENT_NUMBERS_ROW", new DifferentNumbersRow());
        }
    };

    private static final Map<String, String> windowCard = new HashMap<String, String>() {
        {
            put("WINDOW1", "y0y060y2000004b150b0");
            put("WINDOW2", "60y010y0y00b2b014b36");
            put("WINDOW3", "000002y5b10r3p010604");
            put("WINDOW4", "r0b0y4p3g20105000600");
            put("WINDOW5", "103b002b006b040b5201");
            put("WINDOW6", "yb001g050430r0g200by");
            put("WINDOW7", "y0600015023yrp00043r");
            put("WINDOW8", "001001g3b2b546g0b5g0");
            put("WINDOW9", "341500620y000yr50yr6");
            put("WINDOW10", "00r50p40g3600b00y200");
            put("WINDOW11", "30r020g6p0y010b05040");
            put("WINDOW12", "12000gb4100gb6163000");
            put("WINDOW13", "000005r4g66352b00001");
            put("WINDOW14", "000g02r06b054g61r02b");
            put("WINDOW15", "4025g006g203g404g100");
            put("WINDOW16", "00g002y5b10r3p010604");
            put("WINDOW17", "1r30654r20005r10003r");
            put("WINDOW18", "05002b1p060y400p00rp");
            put("WINDOW19", "y0p00042y30y05060p0g");
            put("WINDOW20", "3020r0y40g01605p0r04");
            put("WINDOW21", "1r004052000py3g600r5");
            put("WINDOW22", "g6p4r0050000200r1y3b");
            put("WINDOW23", "50b40p0y300g40r20600");
            put("WINDOW24", "40100g0y300bpr150206");
            put("WINDOW25", "10g050g40pg02p030p06");
        }
    };

    private static final ReentrantLock lock = new ReentrantLock();

    public static void setGame() {
        scores = new HashMap<>();
        round = 1;
        gameDice = new ArrayList<>();
        publicObjectives = new ArrayList<>();
        dratPool = new ArrayList<>();
        setGameDice();
        setPrivateObjectives();
        setPublicObjectives();
        setPlayerWindowChoices();
    }

    public static void playerMove(ClientHandler client, int draftPoolSelection, int windowSelection) {
        lock.lock();
        System.out.println("Draft Pool Selection ---> " + draftPoolSelection);
        System.out.println("Window Selection ---> " + windowSelection);

        for (Player player : players.keySet()) {
            if (client.equals(players.get(player))) {
                Box selectedBox = player.getWindowCard().getWindowGrid().get(windowSelection);
                selectedBox.placeDie(dratPool.remove(draftPoolSelection));
            }
        }
        endTurn();
        lock.unlock();
    }

    private static void endTurn() {
        lock.lock();
        activePlayer = roundManager.getNextPlayer();
        if (activePlayer == null)
            endRound();
        playerTurn(activePlayer);
        lock.unlock();
    }

    private static void endRound() {
        lock.lock();
        dratPool.clear();
        displayDraftPool();
        round++;
        isFirstRound = false;
        if(round <= 10)
            startRound();
        else
            endGame();
        lock.unlock();
    }

    private static void endGame() {
        for(Player player : players.keySet())
            player.setPoints(pointsCalculator(player));

        int max = -999;
        String winner = "";
        Map<String ,Integer >  playerPoints = new HashMap<>();
        for(Player player : players.keySet()){
            playerPoints.put(player.getName(), player.getPoints());
            if(player.getPoints() > max){
                max = player.getPoints();
                winner  = player.getName();
            }
        }

        notifyPlayers(new EndGame(winner, playerPoints));


    }

    private static int pointsCalculator(Player player){
        int score = 0;
        for(PublicObjective obj :  publicObjectives){
           score += obj.calculatePoints(player.getWindowCard());
        }

        Colour privateObjective = player.getPrivateObjective().getColour();
        for(WindowCardBox windowCardBox : player.getWindowCard().getWindowGrid()){
            if(!windowCardBox.isEmpty()){
                if(windowCardBox.getDie().getColour() == privateObjective){
                    score += 1;
                }
            } else{
                score -= 1;
            }
        }


        return score;
    }

    private static void startRound() {
        lock.lock();
        notifyPlayers(new Round("Round " + round));
        setDraftPool();
        displayDraftPool();
        roundManager.playerOrder();
        activePlayer = roundManager.getNextPlayer();
        playerTurn(activePlayer);
        lock.unlock();
    }

    public static void windowSelection(ClientHandler client, String window) {
        lock.lock();
        for (Player player : players.keySet()) {
            if (client.equals(players.get(player))) {
                player.setWindowCard(new WindowCard(windowCard.get(window)));
                //client.send(new PlayerWindow(window));
            }
        }
        lock.unlock();
    }

    public static void setupDone(ClientHandler clientHandler) {
        lock.lock();
        for (Player player : players.keySet())
            if (clientHandler.equals(players.get(player))) {
                player.setSetupDone(true);
                checkOtherPlayers();
            }
        lock.unlock();
    }

    private static void checkOtherPlayers() {
        lock.lock();
        int playerDone = 0;
        for (Player player : players.keySet())
            if (player.isSetupDone())
                playerDone++;
        if (playerDone == 4) {
            notifyPlayers(new StartGame());
            playGame();
        }
        lock.unlock();
    }

    private static void playGame() {
        lock.lock();
        roundManager = new RoundManager(new ArrayList<>(players.keySet()));
        isFirstRound = true;
        startRound();

        lock.unlock();
    }

    private static void playerTurn(Player player) {
        lock.lock();
        notifyPlayers(new PlayerName(player.getName()));
        displayDraftPool();
       // String colour = player.getPrivateObjective().getColour().toString();
       // players.get(player).send(new PrivateObjectiveReceived(colour));

        for (String windowNumber : windowCard.keySet()){
            if (windowCard.get(windowNumber).equals(player.getWindowCard().getWindowCardId())){
                System.out.println(windowNumber);
                notifyPlayers(new PlayerWindow(windowNumber));
                break;
            }
        }

            if(!isFirstRound)
                displayPlayerDice(player);
        lock.unlock();
    }

    private static void displayDraftPool() {
        lock.lock();
        ArrayList<String> tempDraftPool = new ArrayList<>();
        for (Die die : dratPool) {
            tempDraftPool.add(die.getColour().name() + die.getValue());
        }
        notifyPlayers(new DraftPool(tempDraftPool));
        lock.unlock();
    }

    private static void displayPlayerDice(Player player) {
        lock.lock();
        ArrayList<String> dicePositions = new ArrayList<>();
        WindowCard playerWindow = player.getWindowCard();
        for (int i = 0; i < playerWindow.getWindowGrid().size(); i++) {
            Die curDie = playerWindow.getWindowGrid().get(i).getDie();
            if (curDie.getColour() == null) {
                dicePositions.add("NULL");
            } else {
                switch (curDie.getColour()) {
                    case BLUE:
                        dicePositions.add("BLUE" + curDie.getValue());
                        break;
                    case YELLOW:
                        dicePositions.add("YELLOW" + curDie.getValue());
                        break;
                    case RED:
                        dicePositions.add("RED" + curDie.getValue());
                        break;
                    case GREEN:
                        dicePositions.add("GREEN" + curDie.getValue());
                        break;
                    case PURPLE:
                        dicePositions.add("PURPLE" + curDie.getValue());
                        break;
                    default:
                        dicePositions.add("NULL");
                }
            }

        }

        notifyPlayers(new PlayerWindowState(dicePositions));
        lock.unlock();
    }

    private static void notifyPlayers(Message message) {
        lock.lock();
        for (Player player : players.keySet()) {
            players.get(player).send(message);
        }
        lock.unlock();
    }

    private static void setDraftPool() {
        lock.lock();
        Collections.shuffle(gameDice);
        for (int i = 0; i < 9; i++) {
            if (gameDice.size() > 0) {
                dratPool.add(gameDice.remove(i));
            }

        }
        lock.unlock();
    }


    private static void setPlayerWindowChoices() {
        ArrayList<String> list = new ArrayList<>(windowCard.keySet());
        Collections.shuffle(list);

        ArrayList<String> windowSet1, windowSet2, windowSet3, windowSet4;
        windowSet1 = new ArrayList<>();
        windowSet2 = new ArrayList<>();
        windowSet3 = new ArrayList<>();
        windowSet4 = new ArrayList<>();

        ArrayList<ArrayList<String>> choices = new ArrayList<>(Arrays.asList(windowSet1, windowSet2, windowSet3, windowSet4));

        for (int i = 0; i < 16; i++) {
            switch (i) {
                case 0:
                case 1:
                case 2:
                case 3:
                    windowSet1.add(list.get(i));
                    break;
                case 4:
                case 5:
                case 6:
                case 7:
                    windowSet2.add(list.get(i));
                    break;
                case 8:
                case 9:
                case 10:
                case 11:
                    windowSet3.add(list.get(i));
                    break;
                case 12:
                case 13:
                case 14:
                case 15:
                    windowSet4.add(list.get(i));
                    break;
            }
        }

        int i = 0;
        for (Player player : players.keySet()) {
            player.setWindowChoices(choices.get(i++));
        }
    }

    public static void getPlayerWindowsList(ClientHandler client) {
        lock.lock();
        for (Player player : players.keySet()) {
            if (client.equals(players.get(player)))
                client.send(new WindowList(player.getWindowChoices()));
        }
        lock.unlock();
    }

    /**
     * Sets the 3 random public objectives
     **/
    private static void setPublicObjectives() {

        ArrayList<String> listPublicObj = new ArrayList<>(publicObjectiveList.keySet());
        Collections.shuffle(listPublicObj);

        for (int i = 0; i < 3; i++) {
            publicObjectives.add(publicObjectiveList.get(listPublicObj.get(i)));
        }
    }

    public static void getPublicObjectives(ClientHandler client) {
        lock.lock();
        ArrayList<String> tempList = new ArrayList<>();
        int i = 0;
        for (PublicObjective obj : publicObjectives) {
            for (String objKey : publicObjectiveList.keySet()) {
                if (publicObjectiveList.get(objKey).equals(obj)) {
                    tempList.add(objKey);
                    break;
                }
            }
        }

        System.out.println();
        client.send(new PublicObjectives(tempList));
        lock.unlock();
    }

    /**
     * Allocates private objective to each player
     **/
    private static void setPrivateObjectives() {
        ArrayList<String> listColours = new ArrayList<>(colours.keySet());
        Collections.shuffle(listColours);
        int i = 0;
        for (Player player : players.keySet()) {
            player.setPrivateObjective(new PrivateObjective(colours.get(listColours.get(i))));
            i++;
        }

    }

    public static void getPrivateObjective(ClientHandler client) {
        lock.lock();
        for (Player player : players.keySet()) {
            if (client.equals(players.get(player))) {
                String colour = player.getPrivateObjective().getColour().toString().toUpperCase();
                System.out.println(player.getName() + " >>> " + colour);
                client.send(new PrivateObjectiveReceived(colour));
                break;
            }
        }
        lock.unlock();
    }

    /**
     * Create the 90 dice to be used in the game
     **/
    private static void setGameDice() {
        for (int i = 0; i < 3; i++) {
            for (String colour : colours.keySet()) {
                for (int j = 1; j <= 6; j++) {
                    Die newDie = new Die(colours.get(colour), j);
                    gameDice.add(newDie);
                }
            }
        }
    }

}
