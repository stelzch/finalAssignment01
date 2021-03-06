package edu.kit.usxim.FinalAssignment1.exceptions;

public class InvalidDiceNumberException extends GameException {
    /**
     * Create a new InvalidPlacementException
     */
    public InvalidDiceNumberException() {
        super("the dice number was out of bounds");
    }

    /**
     * Custom error message
     * @param str the error msg
     */
    public InvalidDiceNumberException(String str) {
        super(str);
    }
}
