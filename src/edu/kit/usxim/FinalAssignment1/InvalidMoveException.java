package edu.kit.usxim.FinalAssignment1;

public class InvalidMoveException extends Exception {
    /**
     * Create a new InvalidMoveException
     */
    public InvalidMoveException() {
        super("the move was invalid");
    }

    /**
     * Create InvalidMoveException with specific error message
     * @param move the move that was illegal
     */
    public InvalidMoveException(ElementaryTokenMove move) {
        super(generateErrorMessage(move));
    }

    private static String generateErrorMessage(ElementaryTokenMove move) {
        StringBuilder sb = new StringBuilder("the move to ");
        sb.append(move);
        sb.append(" was illegal");
        return sb.toString();
    }

}
