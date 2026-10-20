package nmu.sagrada.server;

import nmu.sagrada.protocol.ContinueTurn;
import nmu.sagrada.protocol.Difficulty;
import nmu.sagrada.protocol.EndGame;
import nmu.sagrada.protocol.ErrorMessage;
import nmu.sagrada.protocol.GameMode;
import nmu.sagrada.protocol.Join;
import nmu.sagrada.protocol.Joined;
import nmu.sagrada.protocol.LobbyState;
import nmu.sagrada.protocol.Message;
import nmu.sagrada.protocol.MoveRejected;
import nmu.sagrada.protocol.PlaceDie;
import nmu.sagrada.protocol.PlayerSummary;
import nmu.sagrada.protocol.Quit;
import nmu.sagrada.protocol.RematchRequest;
import nmu.sagrada.protocol.SkipTurn;
import nmu.sagrada.protocol.TurnPaused;
import nmu.sagrada.protocol.WindowSelected;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

final class GameCoordinator {
    private static final int LAN_PLAYER_COUNT = 4;
    /**
     * Named after the people behind the game and the building: Antoni Gaudí designed the Sagrada
     * Família, and Adrian Adamescu and Daryl Andrews designed Sagrada. Peter Wocken illustrated it.
     */
    private static final String[] BOT_NAMES = {"Gaudí", "Adrian", "Daryl", "Peter"};
    private final Map<String, ClientHandler> clients = new LinkedHashMap<>();
    private final List<PlayerState> participants = new ArrayList<>();
    private final ScheduledExecutorService botExecutor = Executors.newSingleThreadScheduledExecutor();
    private final Random random = new Random();
    private GameMode mode;
    private String hostId;
    private GameSession game;
    /** Set while a finished bot turn waits for the human to send {@link ContinueTurn}. */
    private String pausedBotId;
    private long pausedRevision;
    private Difficulty botDifficulty = Difficulty.MEDIUM;

    synchronized void onMessage(ClientHandler client, Message message) {
        if (message instanceof Join) handleJoin(client, (Join) message);
        else if (message instanceof Quit) client.close();
        else if (client.getPlayerId() == null) client.send(new ErrorMessage("Join the game first.", true));
        else if (message instanceof WindowSelected) handleWindow(client, (WindowSelected) message);
        else if (message instanceof PlaceDie) handlePlace(client, (PlaceDie) message);
        else if (message instanceof SkipTurn) handleSkip(client);
        else if (message instanceof ContinueTurn) handleContinue();
        else if (message instanceof RematchRequest) handleRematch(client, (RematchRequest) message);
        else client.send(new ErrorMessage("Unsupported message.", false));
    }

    private void handleJoin(ClientHandler client, Join join) {
        if (client.getPlayerId() != null) {
            client.send(new ErrorMessage("This connection has already joined.", true));
            return;
        }
        String name = join.name == null ? "" : join.name.trim();
        if (name.isEmpty() || name.length() > 24 || nameExists(name) || name.matches("(?i)bot \\d+")) {
            client.send(new ErrorMessage("Choose a unique name between 1 and 24 characters.", true));
            return;
        }
        if (join.mode == null) {
            client.send(new ErrorMessage("Choose a game mode.", true));
            return;
        }

        if (mode == null) mode = join.mode;
        if (mode != join.mode) {
            client.send(new ErrorMessage("The server is already hosting a different game mode.", true));
            return;
        }
        if (mode == GameMode.VS_BOTS && !clients.isEmpty()) {
            client.send(new ErrorMessage("A bot match is already in progress.", true));
            return;
        }
        if (mode == GameMode.LAN_MULTIPLAYER && (clients.size() >= LAN_PLAYER_COUNT || game != null)) {
            client.send(new ErrorMessage("The LAN match is full or already in progress.", true));
            return;
        }
        if (mode == GameMode.VS_BOTS && (join.botCount < 1 || join.botCount > 3)) {
            client.send(new ErrorMessage("Choose between 1 and 3 bots.", true));
            return;
        }

        String id = UUID.randomUUID().toString();
        client.setPlayerId(id);
        clients.put(id, client);
        participants.add(new PlayerState(id, name, false));
        if (hostId == null) hostId = id;
        client.send(new Joined(id, id.equals(hostId)));

        if (mode == GameMode.VS_BOTS) {
            botDifficulty = join.difficulty == null ? Difficulty.MEDIUM : join.difficulty;
            addBots(join.botCount);
        }
        broadcastLobby();
        if (mode == GameMode.VS_BOTS || clients.size() == LAN_PLAYER_COUNT) startSetup();
    }

    private void addBots(int count) {
        int nameIndex = 0;
        for (int i = 1; i <= count; i++) {
            String name = null;
            while (name == null && nameIndex < BOT_NAMES.length) {
                String candidate = BOT_NAMES[nameIndex++];
                if (!nameExists(candidate)) name = candidate;
            }
            participants.add(new PlayerState("bot-" + i, name == null ? "Bot " + i : name, true));
        }
    }

    private void startSetup() {
        pausedBotId = null;
        game = new GameSession(participants, new Random(random.nextLong()));
        for (Map.Entry<String, ClientHandler> entry : clients.entrySet()) {
            entry.getValue().send(game.setupFor(entry.getKey()));
        }
    }

    private void handleWindow(ClientHandler client, WindowSelected selected) {
        if (game == null || game.isFinished()) {
            client.send(new ErrorMessage("There is no game waiting for a window choice.", false));
            return;
        }
        if (!game.selectWindow(client.getPlayerId(), selected.windowId)) {
            client.send(new ErrorMessage("Choose one of the four offered windows.", false));
            return;
        }
        if (game.allReady()) {
            game.start();
            broadcast(game.snapshot());
            scheduleBotIfNeeded();
        }
    }

    private void handlePlace(ClientHandler client, PlaceDie place) {
        if (!playing(client)) return;
        MoveOutcome outcome = game.place(client.getPlayerId(), place.draftIndex, place.cellIndex);
        if (!outcome.accepted) {
            client.send(new MoveRejected(outcome.code, outcome.message, game.snapshot()));
            return;
        }
        broadcast(game.snapshot());
        advanceAndContinue();
    }

    private void handleSkip(ClientHandler client) {
        if (!playing(client)) return;
        MoveOutcome outcome = game.skip(client.getPlayerId());
        if (!outcome.accepted) {
            client.send(new MoveRejected(outcome.code, outcome.message, game.snapshot()));
            return;
        }
        advanceAndContinue();
    }

    private boolean playing(ClientHandler client) {
        if (game == null || game.isFinished()) {
            client.send(new ErrorMessage("There is no active match.", false));
            return false;
        }
        return true;
    }

    private void advanceAndContinue() {
        game.advanceTurn();
        if (game.isFinished()) {
            broadcast(new EndGame(game.standings()));
            return;
        }
        broadcast(game.snapshot());
        scheduleBotIfNeeded();
    }

    private void scheduleBotIfNeeded() {
        PlayerState active = game == null ? null : game.activePlayer();
        if (active == null || !active.bot) return;
        long expectedRevision = game.getRevision();
        botExecutor.schedule(() -> performBotMove(active.id, expectedRevision),
                botThinkMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * Bots visibly deliberate for two to five seconds before moving, longer at higher difficulty,
     * so a turn reads as a decision rather than an instant state change.
     */
    private long botThinkMillis() {
        switch (botDifficulty) {
            case EASY: return 2000L + random.nextInt(500);
            case HARD: return 4000L + random.nextInt(1000);
            default: return 3000L + random.nextInt(700);
        }
    }

    private synchronized void performBotMove(String botId, long expectedRevision) {
        if (game == null || game.isFinished() || game.getRevision() != expectedRevision) return;
        PlayerState active = game.activePlayer();
        if (active == null || !active.bot || !active.id.equals(botId)) return;
        LegalMove move = game.chooseBotMove(botDifficulty, random);
        boolean placed = move != null;
        if (placed) {
            game.place(botId, move.draftIndex, move.cellIndex);
        } else {
            game.skip(botId);
        }
        broadcast(game.snapshot());
        // Hold the turn open rather than advancing on a timer, so the human decides when play moves
        // on and a bot's move can actually be read.
        pausedBotId = botId;
        pausedRevision = game.getRevision();
        broadcast(new TurnPaused(botId, placed));
    }

    private void handleContinue() {
        if (game == null || game.isFinished() || pausedBotId == null) return;
        if (game.getRevision() != pausedRevision) return;
        pausedBotId = null;
        advanceAndContinue();
    }

    private void handleRematch(ClientHandler client, RematchRequest request) {
        if (game == null || !game.isFinished()) {
            client.send(new ErrorMessage("Finish the current match before starting a rematch.", false));
            return;
        }
        if (!client.getPlayerId().equals(hostId)) {
            client.send(new ErrorMessage("Only the host can start a rematch.", false));
            return;
        }
        if (mode == GameMode.VS_BOTS) {
            if (request.botCount < 1 || request.botCount > 3) {
                client.send(new ErrorMessage("Choose between 1 and 3 bots.", false));
                return;
            }
            botDifficulty = request.difficulty == null ? botDifficulty : request.difficulty;
            participants.removeIf(player -> player.bot);
            addBots(request.botCount);
        }
        startSetup();
        broadcastLobby();
    }

    synchronized void onDisconnect(ClientHandler client) {
        String id = client.getPlayerId();
        if (id == null || !clients.containsKey(id)) return;
        clients.remove(id);
        participants.removeIf(player -> player.id.equals(id));
        pausedBotId = null;
        if (mode == GameMode.VS_BOTS || clients.isEmpty()) {
            clients.clear();
            participants.clear();
            game = null;
            mode = null;
            hostId = null;
            return;
        }
        if (game != null) {
            game = null;
            broadcast(new ErrorMessage("A player disconnected. The match returned to the LAN lobby.", false));
        }
        if (id.equals(hostId)) hostId = clients.keySet().iterator().next();
        broadcastLobby();
    }

    private boolean nameExists(String name) {
        for (PlayerState player : participants) if (player.name.equalsIgnoreCase(name)) return true;
        return false;
    }

    private void broadcastLobby() {
        List<PlayerSummary> summaries = new ArrayList<>();
        for (PlayerState player : participants) summaries.add(new PlayerSummary(player.id, player.name, player.bot));
        broadcast(new LobbyState(mode, summaries, mode == GameMode.LAN_MULTIPLAYER ? LAN_PLAYER_COUNT : 1));
    }

    private void broadcast(Message message) {
        for (ClientHandler client : clients.values()) client.send(message);
    }
}
