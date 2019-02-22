package edu.kit.usxim.FinalAssignment1;

import edu.kit.usxim.FinalAssignment1.exceptions.GameException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCommandException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;
import edu.kit.usxim.FinalAssignment1.exceptions.ProgramQuitRequestException;

import java.util.ArrayList;
import java.util.List;

public class UserInputParser {
    private static final String COMMAND_ARG_SEPERATOR = " ";
    private static final String REGEX_SINGLE_COORDINATE_TUPLE = "(0|(-?[1-9][0-9]*));(0|(-?[1-9][0-9]*))";
    private static final String REGEX_COORDINATE_TUPLE_LIST
            = "^" + REGEX_SINGLE_COORDINATE_TUPLE + "(:" + REGEX_SINGLE_COORDINATE_TUPLE + ")*$";
    private static final String REGEX_COMMAND = "^\\S+( \\S+)?\n?$";

    private PlayerCommandExecutor executor;

    /**
     * Default constructor
     */
    public UserInputParser() {
        this.executor = new Game();
    }

    private void throwErrorForInvalidCoordinate(String input) throws InvalidCoordinatesException {
        throw new InvalidCoordinatesException("not a valid coordinate: " + input);
    }

    private void throwErrorArgsExpectedEmpty(String args) throws InvalidCommandException {
        if (!args.isEmpty())
            throw new InvalidCommandException("did not expect any arguments");
    }

    private void throwErrorArgsExpected(String args) throws InvalidCommandException {
        if (args.isEmpty())
            throw new InvalidCommandException("this command needs an argument");
    }

    private void throwErrorNegativeNull(String param) throws InvalidCoordinatesException {
        if (param.equals("-0"))
            throw new InvalidCoordinatesException("not a number: -0");
    }

    /**
     * Parse a coordinate tuple into integers
     * @param input the user input of two numbers separated by a semicolon
     * @return an elementary token move
     * @throws InvalidCoordinatesException if the provided input is malformed
     */
    public Coordinates parseCoordinates(String input) throws InvalidCoordinatesException {
        if (!input.matches("^" + REGEX_SINGLE_COORDINATE_TUPLE + "$"))
            throwErrorForInvalidCoordinate(input);

        String[] parts = input.split(";");
        if (parts.length != 2)
            throwErrorForInvalidCoordinate(input);

        throwErrorNegativeNull(parts[0]);
        throwErrorNegativeNull(parts[1]);
        try {
            int coordY = Integer.parseInt(parts[0]);
            int coordX = Integer.parseInt(parts[1]);
            Coordinates coords = new Coordinates(coordX, coordY);
            return coords;
        } catch (NumberFormatException e) {
            throwErrorForInvalidCoordinate(input);
        }

        return null;
    }

    /**
     * Parse a list of Coordinates
     * @param input an input string of the form m1;n1:m2;n2:[...]
     * @return a list of elementary token moves
     * @throws InvalidCoordinatesException if the coords were invalid
     * @throws InvalidCommandException if the command format was wrong
     */
    public List<ElementaryTokenMove> parseCoordinateList(String input)
            throws InvalidCoordinatesException, InvalidCommandException {
        if (!input.matches(REGEX_COORDINATE_TUPLE_LIST))
            throw new InvalidCommandException("invalid coordinate format - must be m1;n1:m2;n2 and so on");

        String[] coordinateList = input.split(":");
        List<ElementaryTokenMove> result = new ArrayList<>();

        for (String coordinate : coordinateList)
            result.add(new ElementaryTokenMove(parseCoordinates(coordinate)));

        return result;
    }

    private void throwErrorForInvalidCommandFormat() throws InvalidCommandException {
        throw new InvalidCommandException("invalid command format - should be the command name and optional arguments"
            + " separated by a single space");
    }

    /**
     * @param input the user input
     * @return a string array with two entries, the first being the command name and the second its arguments
     * @throws InvalidCommandException if the command format was wrong
     */
    public String[] parseCommandIntoNameAndArgs(String input) throws InvalidCommandException {
        String[] result = new String[2];
        String[] commandSegments = input.split(COMMAND_ARG_SEPERATOR);

        if (!input.matches(REGEX_COMMAND))
            throwErrorForInvalidCommandFormat();

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
     * @throws GameException if something goes wrong
     */
    public String parseInput(String line) throws GameException {
        String[] commandParts = parseCommandIntoNameAndArgs(line);
        String commandName = commandParts[0];
        String commandArgs = commandParts[1];

        switch (commandName) {
            case "print":
                throwErrorArgsExpectedEmpty(commandArgs);
                return executor.print();
            case "state":
                throwErrorArgsExpected(commandArgs);
                Coordinates coords = parseCoordinates(commandArgs);
                return executor.state(coords);
            case "roll":
                throwErrorArgsExpected(commandArgs);
                return executor.roll(commandArgs);
            case "place":
                throwErrorArgsExpected(commandArgs);
                List<ElementaryTokenMove> placeCoords = parseCoordinateList(commandArgs);
                if (placeCoords.size() != 2)
                    throw new InvalidCoordinatesException("must provide start and end: place <m1>;<n1>:<m2>;<n2>");
                Coordinates start = placeCoords.get(0);
                Coordinates end = placeCoords.get(1);
                return executor.place(start, end);
            case "move":
                throwErrorArgsExpected(commandArgs);
                List<ElementaryTokenMove> moves = parseCoordinateList(commandArgs);
                return executor.move(moves);
            case "set-vc":
                throwErrorArgsExpected(commandArgs);
                Coordinates target = parseCoordinates(commandArgs);
                return executor.setVC(target);
            case "reset":
                throwErrorArgsExpectedEmpty(commandArgs);
                this.executor = new Game();
                return "OK";
            case "quit":
                throwErrorArgsExpectedEmpty(commandArgs);
                throw new ProgramQuitRequestException("user requested exit");
            case "show-result":
                throwErrorArgsExpectedEmpty(commandArgs);
                return String.valueOf(this.executor.result());
            default:
                throw new InvalidCommandException("did not recognise command: " + commandName);
        }
    }
}
