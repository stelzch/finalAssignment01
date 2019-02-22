package edu.kit.usxim.FinalAssignment1.exceptions;

import edu.kit.usxim.FinalAssignment1.Coordinates;

public class InvalidCommandException extends GameException {
    /**
     * Custom error message
     * @param str the error msg
     */
    public InvalidCommandException(String str) {
        super(str);
    }

}
