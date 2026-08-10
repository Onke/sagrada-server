package nmu.sagrada.server;

import nmu.sagrada.core.Colour;
import nmu.sagrada.core.Die;
import nmu.sagrada.core.PlacementValidator;
import nmu.sagrada.core.ScoreCalculator;
import nmu.sagrada.core.WindowCard;
import nmu.sagrada.protocol.Difficulty;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Chooses a bot's placement.
 *
 * <p>The ladder is deliberately simple and legible: EASY plays at random, MEDIUM plays greedily on
 * the resulting score but picks among its top few options so it is beatable, and HARD breaks
 * score ties by keeping the most future placements open.
 */
final class BotStrategy {

    private static final PlacementValidator VALIDATOR = new PlacementValidator();
    private static final int MEDIUM_CHOICE_POOL = 3;

    private BotStrategy() {}

    static LegalMove choose(Difficulty difficulty, WindowCard window, List<Die> pool,
                            List<LegalMove> moves, Colour privateColour,
                            List<String> publicObjectives, Random random) {
        if (moves == null || moves.isEmpty()) return null;
        Difficulty level = difficulty == null ? Difficulty.MEDIUM : difficulty;
        if (level == Difficulty.EASY) return moves.get(random.nextInt(moves.size()));

        List<LegalMove> best = new ArrayList<>();
        List<LegalMove> ranked = new ArrayList<>();
        int bestScore = Integer.MIN_VALUE;
        int[] scores = new int[moves.size()];

        for (int i = 0; i < moves.size(); i++) {
            LegalMove move = moves.get(i);
            WindowCard after = apply(window, pool.get(move.draftIndex), move.cellIndex);
            scores[i] = ScoreCalculator.score(after, privateColour, publicObjectives).getTotal();
            if (scores[i] > bestScore) {
                bestScore = scores[i];
                best.clear();
                best.add(move);
            } else if (scores[i] == bestScore) {
                best.add(move);
            }
        }

        if (level == Difficulty.MEDIUM) {
            // Take a random pick from the strongest handful rather than always the very best.
            for (int i = 0; i < moves.size(); i++) if (scores[i] == bestScore) ranked.add(moves.get(i));
            int span = Math.min(MEDIUM_CHOICE_POOL, ranked.size());
            return ranked.get(random.nextInt(span));
        }

        if (best.size() == 1) return best.get(0);
        return mostFlexible(window, pool, best, random);
    }

    /** Among equally scoring moves, prefer the one leaving the most legal placements available. */
    private static LegalMove mostFlexible(WindowCard window, List<Die> pool,
                                          List<LegalMove> candidates, Random random) {
        List<LegalMove> best = new ArrayList<>();
        int bestMobility = Integer.MIN_VALUE;
        for (LegalMove move : candidates) {
            WindowCard after = apply(window, pool.get(move.draftIndex), move.cellIndex);
            int mobility = 0;
            for (int dieIndex = 0; dieIndex < pool.size(); dieIndex++) {
                if (dieIndex == move.draftIndex) continue;
                for (int cell = 0; cell < WindowCard.SIZE; cell++) {
                    if (VALIDATOR.validate(after, pool.get(dieIndex), cell).isLegal()) mobility++;
                }
            }
            if (mobility > bestMobility) {
                bestMobility = mobility;
                best.clear();
                best.add(move);
            } else if (mobility == bestMobility) {
                best.add(move);
            }
        }
        return best.get(random.nextInt(best.size()));
    }

    /** {@link WindowCard#place} mutates, so candidates are evaluated against a rebuilt copy. */
    private static WindowCard apply(WindowCard window, Die die, int cellIndex) {
        WindowCard copy = new WindowCard(window.getPattern());
        List<String> placed = window.diceIds();
        for (int i = 0; i < placed.size(); i++) {
            if ("NULL".equals(placed.get(i))) continue;
            copy.place(i, Die.fromId(placed.get(i)));
        }
        copy.place(cellIndex, die);
        return copy;
    }
}
