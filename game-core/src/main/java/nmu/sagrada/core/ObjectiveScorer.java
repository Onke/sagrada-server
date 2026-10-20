package nmu.sagrada.core;

import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public final class ObjectiveScorer {
    public static final String UPSIDE_DOWN_NUMBER_PYRAMID = "UPSIDE_DOWN_NUMBER_PYRAMID";
    public static final String THREE_FOUR_PAIR = "THREE_FOUR_PAIR";
    public static final String DIFFERENT_NUMBERS_COLUMN = "DIFFERENT_NUMBERS_COLUMN";
    public static final String DIFFERENT_COLOURS_COLUMN = "DIFFERENT_COLOURS_COLUMN";
    public static final String FIVE_SIX_PAIR = "FIVE_SIX_PAIR";
    public static final String JAGGED_DIFFERENT_COLOURS = "JAGGED_DIFFERENT_COLOURS";
    public static final String DIFFERENT_COLOURS_ROW = "DIFFERENT_COLOURS_ROW";
    public static final String LIGHT_SHADES = "LIGHT_SHADES";
    public static final String ONE_TWO = "ONE_TWO";
    public static final String DIFFERENT_NUMBERS_ROW = "DIFFERENT_NUMBERS_ROW";

    public static final String[] ALL = {UPSIDE_DOWN_NUMBER_PYRAMID, THREE_FOUR_PAIR,
            DIFFERENT_NUMBERS_COLUMN, DIFFERENT_COLOURS_COLUMN, FIVE_SIX_PAIR,
            JAGGED_DIFFERENT_COLOURS, DIFFERENT_COLOURS_ROW, LIGHT_SHADES,
            ONE_TWO, DIFFERENT_NUMBERS_ROW};

    private ObjectiveScorer() {}

    public static int score(String objective, WindowCard window) {
        switch (objective) {
            case UPSIDE_DOWN_NUMBER_PYRAMID: return exactValues(window,
                    new int[]{5, 7, 9, 11, 13, 17}, new int[]{1, 3, 5, 2, 4, 6}) ? 8 : 0;
            case THREE_FOUR_PAIR: return pairScore(window, 3, 4);
            case DIFFERENT_NUMBERS_COLUMN: return variedLines(window, false, false, 4);
            case DIFFERENT_COLOURS_COLUMN: return variedLines(window, false, true, 5);
            case FIVE_SIX_PAIR: return pairScore(window, 5, 6);
            case JAGGED_DIFFERENT_COLOURS: return jaggedColours(window);
            case DIFFERENT_COLOURS_ROW: return variedLines(window, true, true, 6);
            case LIGHT_SHADES: return blueYellowPattern(window) ? 8 : 0;
            case ONE_TWO: return pairScore(window, 1, 2);
            case DIFFERENT_NUMBERS_ROW: return variedLines(window, true, false, 5);
            default: throw new IllegalArgumentException("Unknown objective: " + objective);
        }
    }

    private static int pairScore(WindowCard window, int first, int second) {
        int firstCount = 0;
        int secondCount = 0;
        for (Cell cell : window.getCells()) {
            if (cell.isEmpty()) continue;
            if (cell.getDie().getValue() == first) firstCount++;
            if (cell.getDie().getValue() == second) secondCount++;
        }
        return Math.min(firstCount, secondCount) * 2;
    }

    private static int variedLines(WindowCard window, boolean rows, boolean colours, int points) {
        int lineCount = rows ? WindowCard.ROWS : WindowCard.COLUMNS;
        int lineSize = rows ? WindowCard.COLUMNS : WindowCard.ROWS;
        int score = 0;
        for (int line = 0; line < lineCount; line++) {
            Set<Object> values = new HashSet<>();
            boolean complete = true;
            for (int offset = 0; offset < lineSize; offset++) {
                int index = rows ? line * WindowCard.COLUMNS + offset : offset * WindowCard.COLUMNS + line;
                Cell cell = window.getCell(index);
                if (cell.isEmpty()) { complete = false; break; }
                values.add(colours ? cell.getDie().getColour() : cell.getDie().getValue());
            }
            if (complete && values.size() == lineSize) score += points;
        }
        return score;
    }

    private static boolean exactValues(WindowCard window, int[] indexes, int[] values) {
        for (int i = 0; i < indexes.length; i++) {
            Cell cell = window.getCell(indexes[i]);
            if (cell.isEmpty() || cell.getDie().getValue() != values[i]) return false;
        }
        return true;
    }

    private static int jaggedColours(WindowCard window) {
        int score = 0;
        int[][] groups = {{0, 2, 4, 6, 8}, {5, 7, 9, 11, 13}, {10, 12, 14, 16, 18}};
        for (int[] group : groups) {
            Set<Colour> colours = EnumSet.noneOf(Colour.class);
            boolean complete = true;
            for (int index : group) {
                Cell cell = window.getCell(index);
                if (cell.isEmpty()) { complete = false; break; }
                colours.add(cell.getDie().getColour());
            }
            if (complete && colours.size() == 5) score += 6;
        }
        return score;
    }

    private static boolean blueYellowPattern(WindowCard window) {
        int[] blue = {6, 10, 12, 18};
        int[] yellow = {9, 13, 17};
        for (int index : blue) if (!hasColour(window, index, Colour.BLUE)) return false;
        for (int index : yellow) if (!hasColour(window, index, Colour.YELLOW)) return false;
        return true;
    }

    private static boolean hasColour(WindowCard window, int index, Colour colour) {
        Cell cell = window.getCell(index);
        return !cell.isEmpty() && cell.getDie().getColour() == colour;
    }
}
