package edu.kit.usxim.FinalAssignment1;

import edu.kit.informatik.Terminal;

public class UserInputParser {
    private static final String COMMAND_ARG_SEPERATOR = " ";

    private  PlayerCommandExecutor executor;

    /**
     * Default constructor
     */
    public UserInputParser(PlayerCommandExecutor executor) {
        this.executor = executor;
    }

    private void throwErrorForInvalidCoordinate(String input) {
        throw new IllegalArgumentException("not a valid coordinate: " + input);
    }

    /**
     * Parse a coordinate tuple into integers
     * @param input the user input of two numbers separated by a semicolon
     * @return an int array with 2 entries, being the numbers given by input
     * @throws IllegalArgumentException if the provided input is malformed
     */
    public int[] parseCoordinates(String input) {
        int[] result = new int[2];
        if (!input.matches("[0-9]+;[0-9]+"))
            throwErrorForInvalidCoordinate(input);

        String[] parts = input.split(";");
        if (parts.length != 2)
            throwErrorForInvalidCoordinate(input);

        try {
            result[0] = Integer.parseInt(parts[0]);
            result[1] = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throwErrorForInvalidCoordinate(input);
        }

        return result;
    }

    private void throwErrorForInvalidCommandFormat() {
        throw new IllegalArgumentException("invalid command format - should be this: <commandName> <argument(s)>");
    }

    /**
     * @param input the user input
     * @return a string array with two entries, the first being the command name and the second its arguments
     */
    public String[] parseCommandIntoNameAndArgs(String input) {
        String[] result = new String[2];
        String[] commandSegments = input.split(COMMAND_ARG_SEPERATOR);

        if (commandSegments.length > 2)
            throwErrorForInvalidCommandFormat();

        result[0] = commandSegments[0];
        result[1] = (commandSegments.length == 2) ? commandSegments[1] : "";

        if (result[0].isEmpty() && !result[1].isEmpty())
            throwErrorForInvalidCommandFormat();

        return result;
    }

    /**
     * Parse the provided input and execute it
     * @param line the line the user inputs
     * @return any output the routine provides
     */
    public String parseInput(String line) {
        String[] commandParts = parseCommandIntoNameAndArgs(line);
        String commandName = commandParts[0];
        String commandArgs = commandParts[1];

        switch (commandName) {
            case "print":
                return executor.print();
            case "state":
            default:
                throw new IllegalArgumentException("did not recognise command");
        }
    }
}
