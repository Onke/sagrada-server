package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.board.Box;
import nmu.sagrada.board.WindowCard;

public class DifferentColoursColumn extends PublicObjective {

    @Override
    public int getNumber() {
        return DIFFERENT_COLOURS_COLUMN;
    }

    @Override
    public int calculatePoints(WindowCard windowCard) {
        int points = 0;
        int[] colours = {0, 0, 0, 0, 0};
        for (int i = 0; i < 5; i++) {
            for (int j = i; j < windowCard.LENGTH; j += 5) {
                Box curBox = windowCard.getWindowGrid().get(j);
                if (!curBox.isEmpty()) {
                    switch (curBox.getDie().getColour()) {
                        case GREEN:
                            colours[0]++;
                            break;
                        case PURPLE:
                            colours[1]++;
                            break;
                        case YELLOW:
                            colours[2]++;
                            break;
                        case BLUE:
                            colours[3]++;
                            break;
                        case RED:
                            colours[4]++;
                            break;
                    }
                }
            }
            if (hasNoRepeat(colours))
                points += 5;
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
        return sum == 4;
    }
}
