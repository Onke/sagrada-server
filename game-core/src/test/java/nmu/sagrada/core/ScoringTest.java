package nmu.sagrada.core;

import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScoringTest {
    @Test void pairObjectivesCountPairsAnywhereOnTheWindow() {
        WindowCard window = new WindowCard("00000000000000000000");
        window.place(0, new Die(Colour.RED, 3));
        window.place(4, new Die(Colour.BLUE, 3));
        window.place(10, new Die(Colour.GREEN, 4));
        assertEquals(2, ObjectiveScorer.score(ObjectiveScorer.THREE_FOUR_PAIR, window));
    }

    @Test void privateObjectiveSumsValuesAndEmptyCellsArePenalised() {
        WindowCard window = new WindowCard("00000000000000000000");
        window.place(0, new Die(Colour.RED, 3));
        window.place(1, new Die(Colour.RED, 6));
        window.place(2, new Die(Colour.BLUE, 5));
        ScoreBreakdown score = ScoreCalculator.score(window, Colour.RED,
                Arrays.asList(ObjectiveScorer.ONE_TWO));
        assertEquals(9, score.getPrivateScore());
        assertEquals(17, score.getEmptyPenalty());
        assertEquals(-8, score.getTotal());
    }

    @Test void completeVarietyLinesScoreAndIncompleteLinesDoNot() {
        WindowCard window = new WindowCard("00000000000000000000");
        window.place(0, new Die(Colour.RED, 1));
        window.place(1, new Die(Colour.BLUE, 2));
        window.place(2, new Die(Colour.GREEN, 3));
        window.place(3, new Die(Colour.PURPLE, 4));
        window.place(4, new Die(Colour.YELLOW, 5));
        assertEquals(6, ObjectiveScorer.score(ObjectiveScorer.DIFFERENT_COLOURS_ROW, window));
        assertEquals(5, ObjectiveScorer.score(ObjectiveScorer.DIFFERENT_NUMBERS_ROW, window));
        assertEquals(0, ObjectiveScorer.score(ObjectiveScorer.DIFFERENT_NUMBERS_COLUMN, window));
    }
}
