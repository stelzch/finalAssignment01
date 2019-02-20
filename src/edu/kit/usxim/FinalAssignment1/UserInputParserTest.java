package edu.kit.usxim.FinalAssignment1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class UserInputParserTest {

    @Test
    public void testSimplePrint() {
        Game g = new Game();
        UserInputParser parser = new UserInputParser(g);

        String output = parser.parseInput("print");
        assertTrue(output.startsWith("-"));
    }

    @Test
    public void testCommandParsing() {
        Game g = new Game();
        UserInputParser parser = new UserInputParser(g);

        String[] result = parser.parseCommandIntoNameAndArgs("print herearetheargs");
        assertEquals(2, result.length);
        assertEquals("print", result[0]);
        assertEquals("herearetheargs", result[1]);
    }

    @Test
    public void testCommandParsingWithoutArgs() {
        Game g = new Game();
        UserInputParser parser = new UserInputParser(g);

        String[] result = parser.parseCommandIntoNameAndArgs("onlyacommand");
        assertEquals(2, result.length);
        assertEquals("onlyacommand", result[0]);
        assertEquals("", result[1]);
    }

    @Test
    public void testCommandParsingWithManySpaceArgs() {
        Game g = new Game();
        UserInputParser parser = new UserInputParser(g);

        String command = "commandName arg1 arg2";
        try {
            String[] result = parser.parseCommandIntoNameAndArgs(command);
        } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("invalid command format"));
            return;
        }

        fail("the userinputparser should throw an error when more than two args are provided");
    }

    @Test
    public void testEmptyCommandParsing() {
        Game g = new Game();
        UserInputParser parser = new UserInputParser(g);

        String[] result = parser.parseCommandIntoNameAndArgs("");

        assertEquals(2, result.length);
        assertEquals("", result[0]);
        assertEquals("", result[1]);
    }

    @Test
    public void testEmptyCommandButArgsparsing() {
        Game g = new Game();
        UserInputParser parser = new UserInputParser(g);

        try {
            String[] result = parser.parseCommandIntoNameAndArgs(" argsonly");
         } catch (IllegalArgumentException e) {
            assertTrue(e.getMessage().startsWith("invalid command format"));
            return;
        }

        fail("if args are provided, there should also be a command name");
    }

    @Test
    public void testCoordinateArg() {
        Game g = new Game();
        UserInputParser parser = new UserInputParser(g);

        int[] result = parser.parseCoordinates("5;33");
        assertEquals(2, result.length);
        assertEquals(5, result[0]);
        assertEquals(33, result[1]);
    }

    @Test
    public void testIllegalCoordinateArg() {
        Executable parseMalformedCoordinateArg = () -> {
            Game g = new Game();
            UserInputParser parser = new UserInputParser(g);

            int[] result = parser.parseCoordinates("5;3a3");
        };

        Executable parseHexCoordinateArg = () -> {
            Game g = new Game();
            UserInputParser parser = new UserInputParser(g);

            int[] result = parser.parseCoordinates("0x03;0x22");
        };

        Executable parseNegativeCoordinateArg = () -> {
            Game g = new Game();
            UserInputParser parser = new UserInputParser(g);

            int[] result = parser.parseCoordinates("5;-5");
        };

        assertThrows(IllegalArgumentException.class, parseMalformedCoordinateArg);
        assertThrows(IllegalArgumentException.class, parseHexCoordinateArg);
        assertThrows(IllegalArgumentException.class, parseNegativeCoordinateArg);
    }

}