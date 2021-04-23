package nmu.sagrada.objectives.publicObjectives;

import nmu.sagrada.board.WindowCard;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BlueYellowPatternTest {
    WindowCard window = new WindowCard("50b40p0y300g40r20600");
    Dice dice = new Dice();
    @Test
    void calculatePoints() {
        window.getWindowGrid().get(0).placeDie(dice.blue.get(4));
        window.getWindowGrid().get(1).placeDie(dice.red.get(3));
        window.getWindowGrid().get(2).placeDie(dice.yellow.get(2));
        window.getWindowGrid().get(3).placeDie(dice.blue.get(5));
        window.getWindowGrid().get(4).placeDie(dice.green.get(6));
        window.getWindowGrid().get(5).placeDie(dice.yellow.get(6));
        window.getWindowGrid().get(6).placeDie(dice.blue.get(3));
        window.getWindowGrid().get(7).placeDie(dice.red.get(6));
        window.getWindowGrid().get(8).placeDie(dice.green.get(1));
        window.getWindowGrid().get(9).placeDie(dice.yellow.get(2));
        window.getWindowGrid().get(10).placeDie(dice.blue.get(1));
        window.getWindowGrid().get(11).placeDie(dice.purple.get(3));
        window.getWindowGrid().get(12).placeDie(dice.blue.get(5));
        window.getWindowGrid().get(13).placeDie(dice.yellow.get(4));
        window.getWindowGrid().get(14).placeDie(dice.blue.get(1));
        window.getWindowGrid().get(15).placeDie(dice.blue.get(4));
        window.getWindowGrid().get(16).placeDie(dice.green.get(2));
        window.getWindowGrid().get(17).placeDie(dice.yellow.get(1));
        window.getWindowGrid().get(18).placeDie(dice.blue.get(2));
        window.getWindowGrid().get(19).placeDie(dice.red.get(4));

        BlueYellowPattern objective = new BlueYellowPattern();
        assert objective.calculatePoints(window) == 8;
    }
}