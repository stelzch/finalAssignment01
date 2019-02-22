package edu.kit.usxim.FinalAssignment1.exceptions;

public class InvalidCommandException extends GameException {
    /**
     * Custom error message
     * @param str the error msg
     */
    public InvalidCommandException(String str) {
        super(str);
    }

}
