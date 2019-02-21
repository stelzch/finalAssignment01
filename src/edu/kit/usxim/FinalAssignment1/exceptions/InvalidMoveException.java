package edu.kit.usxim.FinalAssignment1.exceptions;

import edu.kit.usxim.FinalAssignment1.ElementaryTokenMove;

public class InvalidMoveException extends GameException {
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

    /**
     * Generate the exception with a custom error message
     * @param msg the error message
     */
    public InvalidMoveException(String msg) {
        super(msg);
    }

    private static String generateErrorMessage(ElementaryTokenMove move) {
        StringBuilder sb = new StringBuilder("the move to ");
        sb.append(move);
        sb.append(" was illegal");
        return sb.toString();
    }
}
