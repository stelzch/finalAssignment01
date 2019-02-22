package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Coordinates;
import edu.kit.usxim.FinalAssignment1.ElementaryTokenMove;
import edu.kit.usxim.FinalAssignment1.UserInputParser;
import edu.kit.usxim.FinalAssignment1.exceptions.GameException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCommandException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserInputParserTest {

    @Test
    public void testSimplePrint() throws GameException {
        UserInputParser parser = new UserInputParser();

        String output = parser.parseInput("print");
        assertTrue(output.startsWith("-"));
    }

    @Test
    public void testCommandParsing() throws GameException {
        UserInputParser parser = new UserInputParser();

        String[] result = parser.parseCommandIntoNameAndArgs("print herearetheargs");
        assertEquals(2, result.length);
        assertEquals("print", result[0]);
        assertEquals("herearetheargs", result[1]);
    }

    @Test
    public void testCommandParsingWithoutArgs() throws GameException {
        UserInputParser parser = new UserInputParser();

        String[] result = parser.parseCommandIntoNameAndArgs("onlyacommand");
        assertEquals(2, result.length);
        assertEquals("onlyacommand", result[0]);
        assertEquals("", result[1]);
    }

    @Test
    public void testCommandParsingWithManySpaceArgs() throws GameException {
        UserInputParser parser = new UserInputParser();

        String command = "commandName arg1 arg2";
        try {
            String[] result = parser.parseCommandIntoNameAndArgs(command);
        } catch (InvalidCommandException e) {
            assertTrue(e.getMessage().startsWith("invalid command format"));
            return;
        }

        fail("the userinputparser should throw an error when more than two args are provided");
    }

    @Test
    public void testEmptyCommandParsing() throws GameException {
        UserInputParser parser = new UserInputParser();

        String[] result = parser.parseCommandIntoNameAndArgs("");

        assertEquals(2, result.length);
        assertEquals("", result[0]);
        assertEquals("", result[1]);
    }

    @Test
    public void testEmptyCommandButArgsparsing() throws GameException {
        UserInputParser parser = new UserInputParser();

        try {
            String[] result = parser.parseCommandIntoNameAndArgs(" argsonly");
        } catch (InvalidCommandException e) {
            assertTrue(e.getMessage().startsWith("invalid command format"));
            return;
        }

        fail("if args are provided, there should also be a command name");
    }

    @Test
    public void testCoordinateArg() throws GameException {
        UserInputParser parser = new UserInputParser();

        Coordinates result = parser.parseCoordinates("5;33");
        assertEquals(5, result.getY());
        assertEquals(33, result.getX());
    }

    @Test
    public void testIllegalCoordinateArg() throws GameException {
        Executable parseMalformedCoordinateArg = () -> {
            UserInputParser parser = new UserInputParser();

            Coordinates result = parser.parseCoordinates("5;3a3");
        };

        Executable parseHexCoordinateArg = () -> {
            UserInputParser parser = new UserInputParser();

            Coordinates result = parser.parseCoordinates("0x03;0x22");
        };

        Executable parseNegativeCoordinateArg = () -> {
            UserInputParser parser = new UserInputParser();

            Coordinates result = parser.parseCoordinates("5;-5");
        };

        assertThrows(InvalidCoordinatesException.class, parseMalformedCoordinateArg);
        assertThrows(InvalidCoordinatesException.class, parseHexCoordinateArg);
        assertDoesNotThrow(parseNegativeCoordinateArg);
    }

    @Test
    public void testParseCoordinateList() throws GameException {
        UserInputParser parser = new UserInputParser();

        List<ElementaryTokenMove> coordinates = parser.parseCoordinateList("5;1:23;1:4;2");

        List<ElementaryTokenMove> expected = new ArrayList<>();
        expected.add(new ElementaryTokenMove(1, 5));
        expected.add(new ElementaryTokenMove(1, 23));
        expected.add(new ElementaryTokenMove(2, 4));

        assertIterableEquals(expected, coordinates);
    }

    @Test
    public void testInvalidCoordinateList() throws GameException {
        Executable errorInCoordinate = () -> {
            UserInputParser parser = new UserInputParser();

            List<ElementaryTokenMove> coordinates = parser.parseCoordinateList("5;1:23;1:4;a2");
        };

        Executable endingWithDoublecolon = () -> {
            UserInputParser parser = new UserInputParser();

            List<ElementaryTokenMove> coordinates = parser.parseCoordinateList("5;1:23;1:4;2:");
        };

        assertThrows(InvalidCommandException.class, errorInCoordinate);
        assertThrows(InvalidCommandException.class, endingWithDoublecolon);
    }
}