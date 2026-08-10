package nmu.sagrada.server;

import nmu.sagrada.core.Colour;
import nmu.sagrada.core.Die;
import nmu.sagrada.core.ObjectiveScorer;
import nmu.sagrada.core.PlacementValidator;
import nmu.sagrada.core.ScoreBreakdown;
import nmu.sagrada.core.ScoreCalculator;
import nmu.sagrada.core.ValidationResult;
import nmu.sagrada.core.WindowCard;
import nmu.sagrada.core.WindowPatterns;
import nmu.sagrada.protocol.Difficulty;
import nmu.sagrada.protocol.GameSnapshot;
import nmu.sagrada.protocol.PlayerScore;
import nmu.sagrada.protocol.PlayerSnapshot;
import nmu.sagrada.protocol.SetupState;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

final class GameSession {
    private final List<PlayerState> players;
    private final Random random;
    private final PlacementValidator validator = new PlacementValidator();
    private final List<String> publicObjectives = new ArrayList<>();
    private final List<Colour> diceBag = new ArrayList<>();
    private final List<Die> draftPool = new ArrayList<>();
    private final List<Integer> turnOrder = new ArrayList<>();
    private int round;
    private int startPlayerIndex;
    private int turnCursor;
    private boolean started;
    private boolean finished;
    private long revision;

    GameSession(List<PlayerState> participants, Random random) {
        this.players = new ArrayList<>(participants);
        this.random = random;
        prepareSetup();
    }

    private void prepareSetup() {
        List<Colour> colours = new ArrayList<>(Arrays.asList(Colour.values()));
        Collections.shuffle(colours, random);
        List<String> objectives = new ArrayList<>(Arrays.asList(ObjectiveScorer.ALL));
        Collections.shuffle(objectives, random);
        publicObjectives.addAll(objectives.subList(0, 3));

        List<String> windows = new ArrayList<>(WindowPatterns.ids());
        Collections.shuffle(windows, random);
        for (int i = 0; i < players.size(); i++) {
            PlayerState player = players.get(i);
            player.privateObjective = colours.get(i);
            player.windowId = null;
            player.window = null;
            player.windowChoices = new ArrayList<>(windows.subList(i * 4, i * 4 + 4));
            if (player.bot) selectWindow(player, player.windowChoices.get(random.nextInt(4)));
        }
    }

    SetupState setupFor(String playerId) {
        PlayerState player = player(playerId);
        return new SetupState(player.privateObjective.name(), publicObjectives, player.windowChoices);
    }

    boolean selectWindow(String playerId, String windowId) {
        PlayerState player = player(playerId);
        if (player == null || !player.windowChoices.contains(windowId)) return false;
        selectWindow(player, windowId);
        revision++;
        return true;
    }

    private void selectWindow(PlayerState player, String windowId) {
        player.windowId = windowId;
        player.window = new WindowCard(WindowPatterns.patternFor(windowId));
    }

    boolean allReady() {
        for (PlayerState player : players) if (!player.isReady()) return false;
        return true;
    }

    void start() {
        if (!allReady()) throw new IllegalStateException("All players must choose a window");
        diceBag.clear();
        for (Colour colour : Colour.values()) for (int i = 0; i < 18; i++) diceBag.add(colour);
        Collections.shuffle(diceBag, random);
        round = 1;
        startPlayerIndex = random.nextInt(players.size());
        started = true;
        finished = false;
        startRound();
    }

    private void startRound() {
        draftPool.clear();
        int draftSize = players.size() * 2 + 1;
        for (int i = 0; i < draftSize; i++) {
            Colour colour = diceBag.remove(diceBag.size() - 1);
            draftPool.add(new Die(colour, random.nextInt(6) + 1));
        }
        turnOrder.clear();
        for (int offset = 0; offset < players.size(); offset++) {
            turnOrder.add((startPlayerIndex + offset) % players.size());
        }
        for (int offset = players.size() - 1; offset >= 0; offset--) {
            turnOrder.add((startPlayerIndex + offset) % players.size());
        }
        turnCursor = 0;
        revision++;
    }

    PlayerState activePlayer() {
        if (!started || finished || turnCursor >= turnOrder.size()) return null;
        return players.get(turnOrder.get(turnCursor));
    }

    MoveOutcome place(String playerId, int draftIndex, int cellIndex) {
        PlayerState active = activePlayer();
        if (active == null || !active.id.equals(playerId)) {
            return MoveOutcome.rejected("NOT_YOUR_TURN", "Wait for your turn.");
        }
        if (draftIndex < 0 || draftIndex >= draftPool.size()) {
            return MoveOutcome.rejected("INVALID_DIE", "Choose a die that is still in the draft pool.");
        }
        Die die = draftPool.get(draftIndex);
        ValidationResult validation = validator.validate(active.window, die, cellIndex);
        if (!validation.isLegal()) {
            return MoveOutcome.rejected(validation.getCode().name(), validation.getMessage());
        }
        active.window.place(cellIndex, die);
        draftPool.remove(draftIndex);
        revision++;
        return MoveOutcome.accepted();
    }

    MoveOutcome skip(String playerId) {
        PlayerState active = activePlayer();
        if (active == null || !active.id.equals(playerId)) {
            return MoveOutcome.rejected("NOT_YOUR_TURN", "Wait for your turn.");
        }
        revision++;
        return MoveOutcome.accepted();
    }

    /** Picks the active bot's move at the given difficulty, or null when it has no legal placement. */
    LegalMove chooseBotMove(Difficulty difficulty, Random chooser) {
        PlayerState active = activePlayer();
        if (active == null) return null;
        return BotStrategy.choose(difficulty, active.window, draftPool, legalMoves(),
                active.privateObjective, publicObjectives, chooser);
    }

    List<LegalMove> legalMoves() {
        List<LegalMove> result = new ArrayList<>();
        PlayerState active = activePlayer();
        if (active == null) return result;
        for (int dieIndex = 0; dieIndex < draftPool.size(); dieIndex++) {
            for (int cellIndex = 0; cellIndex < WindowCard.SIZE; cellIndex++) {
                if (validator.validate(active.window, draftPool.get(dieIndex), cellIndex).isLegal()) {
                    result.add(new LegalMove(dieIndex, cellIndex));
                }
            }
        }
        return result;
    }

    void advanceTurn() {
        if (finished) return;
        turnCursor++;
        if (turnCursor < turnOrder.size()) {
            revision++;
            return;
        }
        if (round == 10) {
            finished = true;
            draftPool.clear();
            revision++;
            return;
        }
        round++;
        startPlayerIndex = (startPlayerIndex + 1) % players.size();
        startRound();
    }

    boolean isFinished() { return finished; }
    long getRevision() { return revision; }

    GameSnapshot snapshot() {
        List<String> dice = new ArrayList<>();
        for (Die die : draftPool) dice.add(die.getId());
        List<PlayerSnapshot> snapshots = new ArrayList<>();
        for (PlayerState player : players) {
            List<String> positions = player.window == null
                    ? Collections.nCopies(WindowCard.SIZE, "NULL") : player.window.diceIds();
            snapshots.add(new PlayerSnapshot(player.id, player.name, player.bot,
                    player.windowId == null ? "" : player.windowId, positions));
        }
        PlayerState active = activePlayer();
        return new GameSnapshot(round, active == null ? "" : active.id, dice, snapshots, revision);
    }

    List<PlayerScore> standings() {
        final Map<String, Integer> finalOrderPriority = new HashMap<>();
        for (int i = 0; i < turnOrder.size(); i++) finalOrderPriority.put(players.get(turnOrder.get(i)).id, i);
        List<PlayerScore> result = new ArrayList<>();
        for (PlayerState player : players) {
            ScoreBreakdown score = ScoreCalculator.score(player.window, player.privateObjective, publicObjectives);
            result.add(new PlayerScore(player.id, player.name, player.bot, score.getTotal(),
                    score.getPublicScore(), score.getPrivateScore(), score.getEmptyPenalty(),
                    player.privateObjective == null ? "" : player.privateObjective.name()));
        }
        result.sort(Comparator.comparingInt((PlayerScore score) -> score.total).reversed()
                .thenComparing(Comparator.comparingInt((PlayerScore score) -> score.privateScore).reversed())
                .thenComparing(Comparator.comparingInt((PlayerScore score) ->
                        finalOrderPriority.getOrDefault(score.playerId, 0)).reversed()));
        return result;
    }

    List<PlayerState> players() { return Collections.unmodifiableList(players); }

    private PlayerState player(String id) {
        for (PlayerState player : players) if (player.id.equals(id)) return player;
        return null;
    }
}
