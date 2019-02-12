package edu.kit.usxim.FinalAssignment1;

public class InvalidPlacementException extends Exception {
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
