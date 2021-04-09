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
        int[] colours = {0, 0, 0, 0, 0, 0};
        int index = 0;
        for (int i = 0; i < windowCard.LENGTH / 5; i++) {
            for (int j = 0; j < windowCard.LENGTH / 4; j++) {
                Box curBox = windowCard.getWindowGrid().get(index);
                index++;
                if (!curBox.isEmpty()) {
                    switch (curBox.getDie().getValue()) {
                        case 1:
                            colours[0]++;
                            break;
                        case 2:
                            colours[1]++;
                            break;
                        case 3:
                            colours[2]++;
                            break;
                        case 4:
                            colours[3]++;
                            break;
                        case 5:
                            colours[4]++;
                            break;
                        case 6:
                            colours[5]++;
                            break;
                    }
                }
                colours[0] = 99;
            }
            if (hasNoRepeat(colours))
                points += 5;
            colours = new int[]{0, 0, 0, 0, 0, 0};
        }
        return points;
    }


    private boolean hasNoRepeat(int[] colours) {
        for(int colour : colours)
            if(colour > 1)
                return false;
        return true;
    }
}
