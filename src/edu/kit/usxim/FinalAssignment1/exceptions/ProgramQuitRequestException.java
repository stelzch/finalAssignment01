package edu.kit.usxim.FinalAssignment1.exceptions;

public class ProgramQuitRequestException extends GameException {
    /**
     * Custom error message
     * @param str the error msg
     */
    public ProgramQuitRequestException(String str) {
        super(str);
    }

}
