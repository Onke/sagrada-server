package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.board.Box;
import nmu.sagrada.board.WindowCard;

public class DifferentNumbersRow extends PublicObjective {
    @Override
    public int getNumber() {
        return DIFFERENT_NUMBERS_ROW;
    }

    @Override
    public int calculatePoints(WindowCard windowCard) {
        int points = 0;
        int[] values = {0, 0, 0, 0, 0, 0};
        int index = 0;
        for (int i = 0; i < windowCard.LENGTH / 5; i++) {
            for (int j = 0; j < windowCard.LENGTH / 4; j++) {
                Box curBox = windowCard.getWindowGrid().get(index);
                index++;
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
            }
            if (hasNoRepeat(values))
                points += 5;
            values = new int[]{0, 0, 0, 0, 0, 0};
        }
        return points;
    }


    private boolean hasNoRepeat(int[] colours) {
        int sum = 0;
        for (int colour : colours){
            sum += colour;
            if (colour > 1)
                return false;
        }
        return sum == 5;
    }
}
