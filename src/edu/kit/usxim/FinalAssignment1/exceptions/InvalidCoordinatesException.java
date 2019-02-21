package edu.kit.usxim.FinalAssignment1.exceptions;

import edu.kit.usxim.FinalAssignment1.Coordinates;

public class InvalidCoordinatesException extends GameException {
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

    /**
     * Create with the coordinates in the error message
     * @param coords the coordinates that were faulty
     */
    public InvalidCoordinatesException(Coordinates coords) {
        super("invalid coordinates - " + coords.toString());
    }
}
