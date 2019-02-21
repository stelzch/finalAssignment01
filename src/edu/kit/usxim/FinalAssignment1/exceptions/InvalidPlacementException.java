package edu.kit.usxim.FinalAssignment1.exceptions;

public class InvalidPlacementException extends GameException {
    /**
     * Create a new InvalidPlacementException
     */
    public InvalidPlacementException() {
        super("the chosen position is invalid");
    }

    /**
     * Custom error message
     * @param str the error message
     */
    public InvalidPlacementException(String str) {
        super(str);
    }
}
