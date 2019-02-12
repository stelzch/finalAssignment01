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
     */
    public InvalidDiceNumberException(String str) {
        super(str);
    }
}
