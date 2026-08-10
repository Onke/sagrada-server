package nmu.sagrada.server;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class GameSessionTest {
    @Test void draftSizeAndSnakeOrderWorkForTwoThreeAndFourPlayers() {
        for (int count = 2; count <= 4; count++) {
            GameSession game = readyGame(count, true);
            assertEquals(count * 2 + 1, game.snapshot().draftPool.size());
            List<String> turns = new ArrayList<>();
            for (int i = 0; i < count * 2; i++) {
                turns.add(game.activePlayer().id);
                assertTrue(game.skip(game.activePlayer().id).accepted);
                game.advanceTurn();
            }
            assertEquals(turns.get(0), turns.get(turns.size() - 1));
            assertEquals(turns.get(count - 1), turns.get(count));
            assertEquals(2, game.snapshot().round);
        }
    }

    @Test void matchesWithOneHumanAndOneToThreeBotsFinishTenRounds() {
        for (int bots = 1; bots <= 3; bots++) {
            GameSession game = readyGame(bots + 1, false);
            int safety = 0;
            while (!game.isFinished() && safety++ < 1000) {
                List<LegalMove> moves = game.legalMoves();
                if (moves.isEmpty()) game.skip(game.activePlayer().id);
                else {
                    LegalMove move = moves.get(0);
                    assertTrue(game.place(game.activePlayer().id, move.draftIndex, move.cellIndex).accepted);
                }
                game.advanceTurn();
            }
            assertTrue(game.isFinished());
            assertEquals(bots + 1, game.standings().size());
        }
    }

    @Test void fourHumanLanSetupAndInactivePlayerValidationWork() {
        GameSession game = readyGame(4, false);
        String inactive = game.players().get(0).id.equals(game.activePlayer().id)
                ? game.players().get(1).id : game.players().get(0).id;
        assertFalse(game.skip(inactive).accepted);
        assertEquals("NOT_YOUR_TURN", game.skip(inactive).code);
    }

    private GameSession readyGame(int count, boolean allBots) {
        List<PlayerState> players = new ArrayList<>();
        for (int i = 0; i < count; i++) players.add(new PlayerState("p" + i, "Player " + i, allBots || i > 0));
        GameSession game = new GameSession(players, new Random(100 + count));
        for (PlayerState player : players) {
            if (!player.isReady()) game.selectWindow(player.id, player.windowChoices.get(0));
        }
        game.start();
        return game;
    }
}
