package nmu.sagrada.core;

public final class ValidationResult {
    public enum Code {
        LEGAL, INVALID_CELL, OCCUPIED, FIRST_DIE_MUST_TOUCH_EDGE,
        MUST_TOUCH_EXISTING_DIE, COLOUR_RESTRICTION, VALUE_RESTRICTION,
        ORTHOGONAL_COLOUR_CONFLICT, ORTHOGONAL_VALUE_CONFLICT
    }

    private final Code code;
    private final String message;

    private ValidationResult(Code code, String message) {
        this.code = code;
        this.message = message;
    }

    public static ValidationResult legal() { return new ValidationResult(Code.LEGAL, "Legal placement"); }
    public static ValidationResult rejected(Code code, String message) { return new ValidationResult(code, message); }
    public boolean isLegal() { return code == Code.LEGAL; }
    public Code getCode() { return code; }
    public String getMessage() { return message; }
}
