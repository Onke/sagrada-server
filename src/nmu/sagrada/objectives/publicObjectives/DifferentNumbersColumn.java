package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.board.Box;
import nmu.sagrada.board.WindowCard;

public class DifferentNumbersColumn extends PublicObjective {
    @Override
    public int getNumber() {
        return DIFFERENT_NUMBERS_COLUMN;
    }

    @Override
    public int calculatePoints(WindowCard windowCard) {
        int points = 0;
        int[] values = {0, 0, 0, 0, 0, 0};
        for (int i = 0; i < 5; i++) {
            for (int j = i; j < windowCard.LENGTH; j += 5) {
                Box curBox = windowCard.getWindowGrid().get(j);
                if (!curBox.isEmpty()) {
                    switch (curBox.getDie().getValue()) {
                        case 1:
                            values[0]++;
                            break;
                        case 2:
                            values[1]++;
                            break;
                        case 3:
                            values[2]++;
                            break;
                        case 4:
                            values[3]++;
                            break;
                        case 5:
                            values[4]++;
                            break;
                        case 6:
                            values[5]++;
                            break;
                    }
                }
               // values[j] = 99;
            }
            if (hasNoRepeat(values))
                points += 4;
            values = new int[]{0, 0, 0, 0, 0, 0};
        }
        return points;
    }

    private boolean hasNoRepeat(int[] values) {
        int sum = 0;
        for (int value : values){
            sum += value;
            if (value > 1)
                return false;
        }
        return sum == 4;
    }
}
