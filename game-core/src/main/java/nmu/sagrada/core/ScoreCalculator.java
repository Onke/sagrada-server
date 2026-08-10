package nmu.sagrada.core;

import java.util.List;

public final class ScoreCalculator {
    private ScoreCalculator() {}

    public static ScoreBreakdown score(WindowCard window, Colour privateColour, List<String> objectives) {
        int publicScore = 0;
        for (String objective : objectives) publicScore += ObjectiveScorer.score(objective, window);
        int privateScore = 0;
        for (Cell cell : window.getCells()) {
            if (!cell.isEmpty() && cell.getDie().getColour() == privateColour) {
                privateScore += cell.getDie().getValue();
            }
        }
        return new ScoreBreakdown(publicScore, privateScore, window.emptyCount());
    }
}
