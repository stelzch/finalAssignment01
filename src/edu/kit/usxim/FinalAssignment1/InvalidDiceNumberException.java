package edu.kit.usxim.FinalAssignment1;

public class InvalidDiceNumberException extends Exception {
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
