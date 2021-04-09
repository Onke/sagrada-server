package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.board.Box;
import nmu.sagrada.board.WindowCard;

public class JaggedDifferentColours extends PublicObjective {

    @Override
    public int getNumber() {
        return JAGGED_DIFFERENT_COLOURS;
    }

    @Override
    public int calculatePoints(WindowCard windowCard) {
        int points = 0;
        int[] colours = {0, 0, 0, 0, 0};
        int index = 0;
        int min;
        for (int i = 0; i < windowCard.LENGTH / 5; i++) {
            for (int j = 0; j < windowCard.LENGTH / 4; j++) {
                Box curBox = windowCard.getWindowGrid().get(index);
                index++;
                if (!curBox.isEmpty()) {
                    switch (curBox.getDie().getColour()) {
                        case RED:
                            colours[0]++;
                            break;
                        case YELLOW:
                            colours[1]++;
                            break;
                        case GREEN:
                            colours[2]++;
                            break;
                        case BLUE:
                            colours[3]++;
                            break;
                        case PURPLE:
                            colours[4]++;
                            break;
                    }
                }
            }

        }
        min = colours[0];
        for (int i = 1; i < colours.length; i++) {
            if (colours[i] < min) min = colours[i];
        }
        return min * 4;
    }
}
