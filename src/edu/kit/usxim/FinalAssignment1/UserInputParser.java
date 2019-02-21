package edu.kit.usxim.FinalAssignment1;

import java.util.ArrayList;
import java.util.List;

public class UserInputParser {
    private static final String COMMAND_ARG_SEPERATOR = " ";

    private PlayerCommandExecutor executor;

    /**
     * Default constructor
     */
    public UserInputParser() {
        this.executor = new Game();
    }

    private void throwErrorForInvalidCoordinate(String input) throws IllegalArgumentException{
        throw new IllegalArgumentException("not a valid coordinate: " + input);
    }

    /**
     * Parse a coordinate tuple into integers
     * @param input the user input of two numbers separated by a semicolon
     * @return an elementary token move
     * @throws IllegalArgumentException if the provided input is malformed
     */
    public ElementaryTokenMove parseCoordinates(String input) {
        if (!input.matches("[0-9]+;[0-9]+"))
            throwErrorForInvalidCoordinate(input);

        String[] parts = input.split(";");
        if (parts.length != 2)
            throwErrorForInvalidCoordinate(input);

        try {
            int coordY = Integer.parseInt(parts[0]);
            int coordX = Integer.parseInt(parts[1]);
            ElementaryTokenMove move = new ElementaryTokenMove(coordX, coordY);
            return move;
        } catch (NumberFormatException e) {
            throwErrorForInvalidCoordinate(input);
        }

        return null;
    }

    public List<ElementaryTokenMove> parseCoordinateList(String input) {
        if (!input.matches("[0-9]+;[0-9]+(:[0-9]+;[0-9]+)+"))
            throw new IllegalArgumentException("invalid coordinate format - must be m1;n1:m2;n2 and so on");

        String[] coordinateList = input.split(":");
        List<ElementaryTokenMove> result = new ArrayList<>();

        for (String coordinate : coordinateList)
            result.add(parseCoordinates(coordinate));

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
    public String parseInput(String line) throws InvalidMoveException {
        String[] commandParts = parseCommandIntoNameAndArgs(line);
        String commandName = commandParts[0];
        String commandArgs = commandParts[1];

        switch (commandName) {
            case "print":
                return executor.print();
            case "state":
                ElementaryTokenMove coords = parseCoordinates(commandArgs);
                return executor.state(coords.getDstY(), coords.getDstX());
            case "roll":
                return executor.roll(commandArgs);
            case "place":
                List<ElementaryTokenMove> moves = parseCoordinateList(commandArgs);
                if (moves.size() != 2)
                    throw new IllegalArgumentException("must provide start and end: place <m1>;<n1>:<m2>;<n2>");
                return executor.move(moves);
            case "move":
            default:
                throw new IllegalArgumentException("did not recognise command");
        }
    }
}
