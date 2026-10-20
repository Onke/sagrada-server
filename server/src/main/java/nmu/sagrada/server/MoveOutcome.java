package nmu.sagrada.server;

final class MoveOutcome {
    final boolean accepted;
    final String code;
    final String message;

    private MoveOutcome(boolean accepted, String code, String message) {
        this.accepted = accepted;
        this.code = code;
        this.message = message;
    }

    static MoveOutcome accepted() { return new MoveOutcome(true, "ACCEPTED", "Move accepted"); }
    static MoveOutcome rejected(String code, String message) { return new MoveOutcome(false, code, message); }
}
