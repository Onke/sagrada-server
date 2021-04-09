package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.board.Box;
import nmu.sagrada.board.WindowCard;

public class OneTwo extends PublicObjective{
    @Override
    public int getNumber() {
        return ONE_TWO;
    }

    @Override
    public int calculatePoints(WindowCard windowCard) {
        int points = 0;
        int index = 0;
        for (int i = 0; i < windowCard.LENGTH / 5; i++) {
            for (int j = 0; j < windowCard.LENGTH / 4; j++) {
                Box curBox = windowCard.getWindowGrid().get(index);
                index++;
                if (!curBox.isEmpty()) {
                    if(curBox.getDie().getValue() == 1 && j != 4)
                        if(windowCard.getWindowGrid().get(index).getDie().getValue() == 2)
                            points += 2;
                }
            }
        }
        return points;
    }
}
