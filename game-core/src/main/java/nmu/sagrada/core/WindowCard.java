package nmu.sagrada.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class WindowCard {
    public static final int ROWS = 4;
    public static final int COLUMNS = 5;
    public static final int SIZE = ROWS * COLUMNS;

    private final String pattern;
    private final List<Cell> cells;

    public WindowCard(String pattern) {
        if (pattern == null || pattern.length() != SIZE) {
            throw new IllegalArgumentException("A window pattern must contain exactly 20 cells");
        }
        this.pattern = pattern;
        List<Cell> parsed = new ArrayList<>();
        for (int i = 0; i < pattern.length(); i++) parsed.add(new Cell(pattern.charAt(i)));
        cells = parsed;
    }

    public String getPattern() { return pattern; }
    public List<Cell> getCells() { return Collections.unmodifiableList(cells); }
    public Cell getCell(int index) { return cells.get(index); }
    public boolean isEmpty() {
        for (Cell cell : cells) if (!cell.isEmpty()) return false;
        return true;
    }
    public int emptyCount() {
        int count = 0;
        for (Cell cell : cells) if (cell.isEmpty()) count++;
        return count;
    }
    public void place(int index, Die die) {
        if (!cells.get(index).isEmpty()) throw new IllegalStateException("Cell is occupied");
        cells.get(index).place(die);
    }
    public List<String> diceIds() {
        List<String> result = new ArrayList<>();
        for (Cell cell : cells) result.add(cell.isEmpty() ? "NULL" : cell.getDie().getId());
        return result;
    }
}
