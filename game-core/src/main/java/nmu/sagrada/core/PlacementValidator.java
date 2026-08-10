package nmu.sagrada.core;

public final class PlacementValidator {
    public ValidationResult validate(WindowCard window, Die die, int index) {
        if (window == null || die == null || index < 0 || index >= WindowCard.SIZE) {
            return ValidationResult.rejected(ValidationResult.Code.INVALID_CELL, "Choose a valid die and window cell.");
        }
        Cell target = window.getCell(index);
        if (!target.isEmpty()) {
            return ValidationResult.rejected(ValidationResult.Code.OCCUPIED, "That window cell is already occupied.");
        }

        int row = index / WindowCard.COLUMNS;
        int column = index % WindowCard.COLUMNS;
        if (window.isEmpty()) {
            if (row != 0 && row != WindowCard.ROWS - 1 && column != 0 && column != WindowCard.COLUMNS - 1) {
                return ValidationResult.rejected(ValidationResult.Code.FIRST_DIE_MUST_TOUCH_EDGE,
                        "Your first die must be placed on an edge or corner.");
            }
        } else if (!touchesExisting(window, row, column)) {
            return ValidationResult.rejected(ValidationResult.Code.MUST_TOUCH_EXISTING_DIE,
                    "The die must touch a previously placed die, including diagonally.");
        }

        if (target.getColourRestriction() != null && target.getColourRestriction() != die.getColour()) {
            return ValidationResult.rejected(ValidationResult.Code.COLOUR_RESTRICTION,
                    "This cell requires a " + target.getColourRestriction().name().toLowerCase() + " die.");
        }
        if (target.getValueRestriction() != 0 && target.getValueRestriction() != die.getValue()) {
            return ValidationResult.rejected(ValidationResult.Code.VALUE_RESTRICTION,
                    "This cell requires a die showing " + target.getValueRestriction() + ".");
        }

        int[][] orthogonal = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};
        for (int[] offset : orthogonal) {
            Cell neighbour = cellAt(window, row + offset[0], column + offset[1]);
            if (neighbour == null || neighbour.isEmpty()) continue;
            if (neighbour.getDie().getColour() == die.getColour()) {
                return ValidationResult.rejected(ValidationResult.Code.ORTHOGONAL_COLOUR_CONFLICT,
                        "Dice of the same colour cannot touch side-to-side.");
            }
            if (neighbour.getDie().getValue() == die.getValue()) {
                return ValidationResult.rejected(ValidationResult.Code.ORTHOGONAL_VALUE_CONFLICT,
                        "Dice showing the same value cannot touch side-to-side.");
            }
        }
        return ValidationResult.legal();
    }

    private boolean touchesExisting(WindowCard window, int row, int column) {
        for (int rowOffset = -1; rowOffset <= 1; rowOffset++) {
            for (int columnOffset = -1; columnOffset <= 1; columnOffset++) {
                if (rowOffset == 0 && columnOffset == 0) continue;
                Cell neighbour = cellAt(window, row + rowOffset, column + columnOffset);
                if (neighbour != null && !neighbour.isEmpty()) return true;
            }
        }
        return false;
    }

    private Cell cellAt(WindowCard window, int row, int column) {
        if (row < 0 || row >= WindowCard.ROWS || column < 0 || column >= WindowCard.COLUMNS) return null;
        return window.getCell(row * WindowCard.COLUMNS + column);
    }
}
