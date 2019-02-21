package edu.kit.usxim.FinalAssignment1;

public class InvalidCoordinatesException extends Exception {
    /**
     * Create a new InvalidPlacementException
     */
    public InvalidCoordinatesException() {
        super("the coordinates were illegal");
    }

    /**
     * Custom error message
     * @param str the error msg
     */
    public InvalidCoordinatesException(String str) {
        super(str);
    }
}
