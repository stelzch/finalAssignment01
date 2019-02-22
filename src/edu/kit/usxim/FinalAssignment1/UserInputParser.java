package edu.kit.usxim.FinalAssignment1;

import edu.kit.usxim.FinalAssignment1.exceptions.GameException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCommandException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;
import edu.kit.usxim.FinalAssignment1.exceptions.ProgramQuitRequestException;

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

    private void throwErrorForInvalidCoordinate(String input) throws InvalidCoordinatesException {
        throw new InvalidCoordinatesException("not a valid coordinate: " + input);
    }

    private void throwErrorArgsExpectedEmpty(String args) throws InvalidCommandException {
        if (!args.isEmpty())
            throw new InvalidCommandException("did not expect any arguments");
    }

    /**
     * Parse a coordinate tuple into integers
     * @param input the user input of two numbers separated by a semicolon
     * @return an elementary token move
     * @throws InvalidCoordinatesException if the provided input is malformed
     */
    public Coordinates parseCoordinates(String input) throws InvalidCoordinatesException {
        if (!input.matches("-?[0-9]+;-?[0-9]+"))
            throwErrorForInvalidCoordinate(input);

        String[] parts = input.split(";");
        if (parts.length != 2)
            throwErrorForInvalidCoordinate(input);

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
        if (!input.matches("-?[0-9]+;-?[0-9]+(:-?[0-9]+;-?[0-9]+)*"))
            throw new InvalidCommandException("invalid coordinate format - must be m1;n1:m2;n2 and so on");

        String[] coordinateList = input.split(":");
        List<ElementaryTokenMove> result = new ArrayList<>();

        for (String coordinate : coordinateList)
            result.add(new ElementaryTokenMove(parseCoordinates(coordinate)));

        return result;
    }

    private void throwErrorForInvalidCommandFormat() throws InvalidCommandException {
        throw new InvalidCommandException("invalid command format - should be this: <commandName> <argument(s)>");
    }

    /**
     * @param input the user input
     * @return a string array with two entries, the first being the command name and the second its arguments
     * @throws InvalidCommandException if the command format was wrong
     */
    public String[] parseCommandIntoNameAndArgs(String input) throws InvalidCommandException {
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
     * @throws GameException if something goes wrong
     * @throws IllegalAccessException if a field is overwritten
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
                Coordinates coords = parseCoordinates(commandArgs);
                return executor.state(coords);
            case "roll":
                return executor.roll(commandArgs);
            case "place":
                List<ElementaryTokenMove> placeCoords = parseCoordinateList(commandArgs);
                if (placeCoords.size() != 2)
                    throw new IllegalArgumentException("must provide start and end: place <m1>;<n1>:<m2>;<n2>");
                Coordinates start = placeCoords.get(0);
                Coordinates end = placeCoords.get(1);
                return executor.place(start, end);
            case "move":
                List<ElementaryTokenMove> moves = parseCoordinateList(commandArgs);
                return executor.move(moves);
            case "set-vc":
                Coordinates target = parseCoordinates(commandArgs);
                return executor.setVC(target);
            case "reset":
                throwErrorArgsExpectedEmpty(commandArgs);
                this.executor = new Game();
                return "OK";
            case "quit":
                throwErrorArgsExpectedEmpty(commandArgs);
                throw new ProgramQuitRequestException("user requested exit");
            default:
                throw new InvalidCommandException("did not recognise command: " + commandName);
        }
    }
}
