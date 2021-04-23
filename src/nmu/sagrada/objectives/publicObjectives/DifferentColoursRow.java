package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.board.Box;
import nmu.sagrada.board.WindowCard;

public class DifferentColoursRow extends PublicObjective {
    @Override
    public int getNumber() {
        return DIFFERENT_COLOURS_ROW;
    }

    @Override
    public int calculatePoints(WindowCard windowCard) {
        int points = 0;
        int[] colours = {0, 0, 0, 0, 0};
        int index = 0;
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
              //  colours[0] = 99;
            }
            if (hasNoRepeat(colours))
                points += 6;
            colours = new int[]{0, 0, 0, 0, 0};
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
