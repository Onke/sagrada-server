package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.Colour;
import nmu.sagrada.board.Box;
import nmu.sagrada.board.WindowCard;

public class UpsideDownNumberPyramid extends PublicObjective {


    @Override
    public int getNumber() {
        return UPSIDE_DOWN_NUMBER_PYRAMID;
    }

    @Override
    public int calculatePoints(WindowCard windowCard) {
        int points = 0;
        int index = 0;
        for (int j = 0; j < windowCard.LENGTH; j++) {
            Box curBox = windowCard.getWindowGrid().get(index);
            index++;
            if (!curBox.isEmpty()) {
                switch (j) {
                    case 5:
                        if (curBox.getDie().getValue() == 1)
                            points++;
                        else return 0;
                    case 7:
                        if (curBox.getDie().getValue() == 3)
                            points++;
                        else return 0;
                    case 9:
                        if (curBox.getDie().getValue() == 5)
                            points++;
                        else return 0;
                    case 11:
                        if (curBox.getDie().getValue() == 2)
                            points++;
                        else return 0;
                    case 13:
                        if (curBox.getDie().getValue() == 4)
                            points++;
                        else return 0;
                    case 17:
                        if (curBox.getDie().getValue() == 6)
                            points++;
                        else return 0;

                }
            }

        }

        if (points == 6)
            return 7;
        else
            return 0;
    }

}
