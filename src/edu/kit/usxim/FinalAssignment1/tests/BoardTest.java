package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Board;
import edu.kit.usxim.FinalAssignment1.Coordinates;
import edu.kit.usxim.FinalAssignment1.Token;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidPlacementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    public void testConstructor() {
        Board b = new Board();

        assertNotNull(b);
    }

    @Test
    public void testFieldUnoccupied() throws InvalidPlacementException, InvalidCoordinatesException {
        Board b = new Board();
        b.placeToken(new Token(Token.Type.VESTA, 1), new Coordinates(5, 4), Token.Orientation.HORIZONTAL);

        assertTrue(b.checkFieldUnoccupied(new Coordinates(0, 0)));
        assertFalse(b.checkFieldUnoccupied(new Coordinates(5, 4)));
    }

    @Test
    public void testGetFieldState() throws InvalidCoordinatesException {
        Board b = new Board();

        assertEquals('-', b.getTokenAt(new Coordinates(0, 0)));
    }

    @Test
    public void testFieldBounds() {
        Executable accessOutOfBounds = () -> {
            Board b = new Board();
            assertFalse(b.isFieldOnBoard(new Coordinates(11, 15)));
            char c = b.getTokenAt(new Coordinates(11, 15));
        };
        Executable accessOutOfBoundsNegative = () -> {
            Board b = new Board();
            assertFalse(b.isFieldOnBoard(new Coordinates(-1, 2)));
            char c = b.getTokenAt(new Coordinates(-1, 2));
        };

        assertThrows(InvalidCoordinatesException.class, accessOutOfBounds);
        assertThrows(InvalidCoordinatesException.class, accessOutOfBoundsNegative);
    }

    @Test
    public void testBasicTokenPlacement() throws InvalidPlacementException, InvalidCoordinatesException {
        Board b = new Board();
        Token c = new Token(Token.Type.CERES, 1);
        Token v = new Token(Token.Type.VESTA, 1);
        Token mcSizeFour = new Token(Token.Type.MISSION_CONTROL, 4);

        b.placeToken(v, new Coordinates(3, 2), Token.Orientation.VERTICAL);
        b.placeToken(c, new Coordinates(9, 3), Token.Orientation.HORIZONTAL);
        b.placeToken(mcSizeFour, new Coordinates(1, 1), Token.Orientation.VERTICAL);
        assertEquals('V', b.getTokenAt(new Coordinates(3, 2)));
        assertEquals('+', b.getTokenAt(new Coordinates(1, 4)));

        for (int y = 1; y <= 4; y++)
            assertEquals('+', b.getTokenAt(new Coordinates(1, y)));
    }

    @Test
    public void testPlacementError() {
        Executable vestaCeresStacked = () -> {
            Board b = new Board();
            b.placeToken(
                    new Token(Token.Type.CERES, 1),
                    new Coordinates(1, 1),
                    Token.Orientation.HORIZONTAL);
            b.placeToken(
                    new Token(Token.Type.VESTA, 1),
                    new Coordinates(1, 1),
                    Token.Orientation.VERTICAL);
        };

        Executable overlappingTokens = () -> {
            Board b = new Board();
            b.placeToken(
                    new Token(Token.Type.MISSION_CONTROL, 3),
                    new Coordinates(0, 0), Token.Orientation.VERTICAL);
            b.placeToken(
                    new Token(Token.Type.MISSION_CONTROL, 6),
                    new Coordinates(0, 0), Token.Orientation.HORIZONTAL);
        };

        Executable closeTokens = () -> {
            Board b = new Board();
            b.placeToken(
                    new Token(Token.Type.MISSION_CONTROL, 3),
                    new Coordinates(0, 0), Token.Orientation.VERTICAL);
            b.placeToken(
                    new Token(Token.Type.MISSION_CONTROL, 6),
                    new Coordinates(1, 0), Token.Orientation.HORIZONTAL);
        };

        assertThrows(InvalidPlacementException.class, vestaCeresStacked);
        assertThrows(InvalidPlacementException.class, overlappingTokens);
        assertDoesNotThrow(closeTokens);
    }

    @Test
    public void testPlacementOutOfBoard() {
        Executable placeOverRightMargin = () -> {
            Board b = new Board();
            Token mcSix = new Token(Token.Type.MISSION_CONTROL, 6);

            b.placeToken(mcSix, new Coordinates(11, 0), Token.Orientation.HORIZONTAL);
        };

        Executable placeAtBottomRightCorner = () -> {
            Board b = new Board();
            Token mcSix = new Token(Token.Type.MISSION_CONTROL, 6);
            b.placeToken(mcSix, new Coordinates(14, 5), Token.Orientation.VERTICAL);
        };

        Executable placeAtBottomRightCornerOutByOne = () -> {
            Board b = new Board();
            Token mcSix = new Token(Token.Type.MISSION_CONTROL, 6);
            b.placeToken(mcSix, new Coordinates(14, 6), Token.Orientation.VERTICAL);
        };

        Executable placeAtUpperRightCornerOutByOne = () -> {
            Board b = new Board();
            Token mcSix = new Token(Token.Type.MISSION_CONTROL, 6);
            b.placeToken(mcSix, new Coordinates(10, 0), Token.Orientation.HORIZONTAL);
        };

        assertThrows(InvalidCoordinatesException.class, placeOverRightMargin);
        assertDoesNotThrow(placeAtBottomRightCorner);
        assertThrows(InvalidCoordinatesException.class, placeAtBottomRightCornerOutByOne);
        assertThrows(InvalidCoordinatesException.class, placeAtUpperRightCornerOutByOne);
    }

    @Test
    public void testLegalDawnPlacement() throws InvalidPlacementException, InvalidCoordinatesException {
        Board b = new Board();
        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 5), new Coordinates(0, 3), Token.Orientation.HORIZONTAL);
        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 7), new Coordinates(5, -2), Token.Orientation.VERTICAL);

        assertEquals("-----+---------\n" +
                "-----+---------\n" +
                "-----+---------\n" +
                "++++++---------\n" +
                "-----+---------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------", b.toString());
    }

    @Test
    public void testInvalidDawnPlacement() {
        Executable placeDawnCompletelyOutOfBoard = () -> {
            Board b = new Board();
            Token dawn = new Token(Token.Type.MISSION_CONTROL, 7);

            b.placeToken(dawn, new Coordinates(11, 15), Token.Orientation.VERTICAL);
        };

        assertThrows(InvalidPlacementException.class, placeDawnCompletelyOutOfBoard);
    }

}