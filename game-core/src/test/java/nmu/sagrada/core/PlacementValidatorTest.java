package nmu.sagrada.core;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PlacementValidatorTest {
    private final PlacementValidator validator = new PlacementValidator();

    @Test void firstDieMustTouchAnEdge() {
        WindowCard window = new WindowCard("00000000000000000000");
        assertFalse(validator.validate(window, new Die(Colour.RED, 4), 7).isLegal());
        assertEquals(ValidationResult.Code.FIRST_DIE_MUST_TOUCH_EDGE,
                validator.validate(window, new Die(Colour.RED, 4), 7).getCode());
        assertTrue(validator.validate(window, new Die(Colour.RED, 4), 3).isLegal());
    }

    @Test void laterDiceMayTouchDiagonallyButMustTouchSomething() {
        WindowCard window = new WindowCard("00000000000000000000");
        window.place(0, new Die(Colour.RED, 4));
        assertTrue(validator.validate(window, new Die(Colour.BLUE, 2), 6).isLegal());
        assertEquals(ValidationResult.Code.MUST_TOUCH_EXISTING_DIE,
                validator.validate(window, new Die(Colour.BLUE, 2), 12).getCode());
    }

    @Test void cellRestrictionsAreEnforced() {
        WindowCard colourWindow = new WindowCard("r0000000000000000000");
        assertEquals(ValidationResult.Code.COLOUR_RESTRICTION,
                validator.validate(colourWindow, new Die(Colour.BLUE, 2), 0).getCode());
        assertTrue(validator.validate(colourWindow, new Die(Colour.RED, 2), 0).isLegal());

        WindowCard valueWindow = new WindowCard("60000000000000000000");
        assertEquals(ValidationResult.Code.VALUE_RESTRICTION,
                validator.validate(valueWindow, new Die(Colour.BLUE, 5), 0).getCode());
        assertTrue(validator.validate(valueWindow, new Die(Colour.BLUE, 6), 0).isLegal());
    }

    @Test void orthogonalMatchesAreRejectedAndDiagonalMatchesAllowed() {
        WindowCard window = new WindowCard("00000000000000000000");
        window.place(0, new Die(Colour.BLUE, 4));
        assertEquals(ValidationResult.Code.ORTHOGONAL_COLOUR_CONFLICT,
                validator.validate(window, new Die(Colour.BLUE, 2), 1).getCode());
        assertEquals(ValidationResult.Code.ORTHOGONAL_VALUE_CONFLICT,
                validator.validate(window, new Die(Colour.RED, 4), 5).getCode());
        assertTrue(validator.validate(window, new Die(Colour.BLUE, 4), 6).isLegal());
    }

    @Test void occupiedAndInvalidCellsAreRejected() {
        WindowCard window = new WindowCard("00000000000000000000");
        window.place(0, new Die(Colour.RED, 1));
        assertEquals(ValidationResult.Code.OCCUPIED,
                validator.validate(window, new Die(Colour.BLUE, 2), 0).getCode());
        assertEquals(ValidationResult.Code.INVALID_CELL,
                validator.validate(window, new Die(Colour.BLUE, 2), -1).getCode());
    }
}
