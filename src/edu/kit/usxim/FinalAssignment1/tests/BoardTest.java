package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Board;
import edu.kit.usxim.FinalAssignment1.InvalidPlacementException;
import edu.kit.usxim.FinalAssignment1.Token;
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
    public void testGetFieldState() {
        Board b = new Board();

        assertEquals('-', b.getTokenAt(0, 0));
    }

    @Test
    public void testFieldBounds() {
        Executable accessOutOfBounds = () -> {
            Board b = new Board();
            char c = b.getTokenAt(11, 15);
        };
        Executable accessOutOfBoundsNegative = () -> {
            Board b = new Board();
            char c = b.getTokenAt(-1, 2);
        };

        assertThrows(IllegalArgumentException.class, accessOutOfBounds);
        assertThrows(IllegalArgumentException.class, accessOutOfBoundsNegative);
    }

    @Test
    public void testBasicTokenPlacement() throws InvalidPlacementException {
        Board b = new Board();
        Token c = new Token(Token.Type.CERES, 1);
        Token v = new Token(Token.Type.VESTA, 1);
        Token mcSizeFour = new Token(Token.Type.MISSION_CONTROL, 4);

        b.placeToken(v, 3, 2, Token.Orientation.VERTICAL);
        b.placeToken(c, 9, 3, Token.Orientation.HORIZONTAL);
        b.placeToken(mcSizeFour, 1, 1, Token.Orientation.VERTICAL);
        assertEquals('V', b.getTokenAt(3, 2));
        assertEquals('+', b.getTokenAt(1, 4));

        for (int y = 1; y <= 4; y++)
            assertEquals('+', b.getTokenAt(1, y));
    }

    @Test
    public void testPlacementError() {
        Executable vestaCeresStacked = () -> {
            Board b = new Board();
            b.placeToken(
                    new Token(Token.Type.CERES, 1),
                    1, 1,
                    Token.Orientation.HORIZONTAL);
            b.placeToken(
                    new Token(Token.Type.VESTA, 1),
                    1, 1,
                    Token.Orientation.VERTICAL);
        };

        Executable overlappingTokens = () -> {
            Board b = new Board();
            b.placeToken(
                    new Token(Token.Type.MISSION_CONTROL, 3),
                    0, 0, Token.Orientation.VERTICAL);
            b.placeToken(
                    new Token(Token.Type.MISSION_CONTROL, 6),
                    0, 0, Token.Orientation.HORIZONTAL);
        };

        Executable closeTokens = () -> {
            Board b = new Board();
            b.placeToken(
                    new Token(Token.Type.MISSION_CONTROL, 3),
                    0, 0, Token.Orientation.VERTICAL);
            b.placeToken(
                    new Token(Token.Type.MISSION_CONTROL, 6),
                    1, 0, Token.Orientation.HORIZONTAL);
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

            b.placeToken(mcSix, 11, 0, Token.Orientation.HORIZONTAL);
        };

        Executable placeAtBottomRightCorner = () -> {
            Board b = new Board();
            Token mcSix = new Token(Token.Type.MISSION_CONTROL, 6);
            b.placeToken(mcSix, 14, 5, Token.Orientation.VERTICAL);
        };

        Executable placeAtBottomRightCornerOutByOne = () -> {
            Board b = new Board();
            Token mcSix = new Token(Token.Type.MISSION_CONTROL, 6);
            b.placeToken(mcSix, 14, 6, Token.Orientation.VERTICAL);
        };

        Executable placeAtUpperRightCornerOutByOne = () -> {
            Board b = new Board();
            Token mcSix = new Token(Token.Type.MISSION_CONTROL, 6);
            b.placeToken(mcSix, 10, 0, Token.Orientation.HORIZONTAL);
        };

        assertThrows(InvalidPlacementException.class, placeOverRightMargin);
        assertDoesNotThrow(placeAtBottomRightCorner);
        assertThrows(InvalidPlacementException.class, placeAtBottomRightCornerOutByOne);
        assertThrows(InvalidPlacementException.class, placeAtUpperRightCornerOutByOne);
    }

    @Test
    public void testInvalidDawnPlacement() {
        Executable placeDawnCompletelyOutOfBoard = () -> {
            Board b = new Board();
            Token dawn = new Token(Token.Type.MISSION_CONTROL, 7);

            b.placeToken(dawn, 11, 15, Token.Orientation.VERTICAL);
        };

        assertThrows(InvalidPlacementException.class, placeDawnCompletelyOutOfBoard);
    }

}