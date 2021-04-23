package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.Colour;
import nmu.sagrada.board.Box;
import nmu.sagrada.board.WindowCard;

public class BlueYellowPattern extends PublicObjective {
    @Override
    public int getNumber() {
        return LIGHT_SHADES;
    }

    @Override
    public int calculatePoints(WindowCard windowCard) {
        int points = 0;
        for (int j = 0; j < windowCard.LENGTH; j++) {
            Box curBox = windowCard.getWindowGrid().get(j);
            if (!curBox.isEmpty()) {
                switch (j) {
                    case 6:
                    case 10:
                    case 12:
                    case 18:
                        if (curBox.getDie().getColour() == Colour.BLUE){
                            points++;
                            break;
                        }
                        else
                            return 0;
                    case 9:
                    case 13:
                    case 17:
                        if (curBox.getDie().getColour() == Colour.YELLOW){

                            points++;
                            break;
                        }
                        else
                            return 0;
                }
            }

        }

        if(points == 7)
            return 8;
        else
            return 0;
    }
}
