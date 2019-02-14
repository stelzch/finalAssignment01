package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ElementaryTokenMoveTest {
    @Test
    public void testBasicElementaryMoves() throws InvalidPlacementException, InvalidMoveException {
        Board b = new Board();
        b.placeToken(new Token(Token.Type.CERES, 1), 5, 4, Token.Orientation.VERTICAL);
        assertEquals('C', b.getTokenAt(5, 4));

        List<ElementaryTokenMove> moves = new ArrayList<>();
        moves.add(new ElementaryTokenMove(5, 5));

        b.executeMoves(5, 4, moves);

        assertEquals('C', b.getTokenAt(5, 5));
    }

    @Test
    public void testErrorOnIllegalMove() {
        Executable performIllegalMove = () -> {
            Board b = new Board();
            b.placeToken(new Token(Token.Type.CERES, 1), 5, 4, Token.Orientation.VERTICAL);

            List<ElementaryTokenMove> moves = new ArrayList<>();

            moves.add(new ElementaryTokenMove(9, 9));

            b.executeMoves(5, 4, moves);
        };

        assertThrows(InvalidMoveException.class, performIllegalMove);
    }

    @Test
    public void testIllegalMoveThroughOtherToken() throws InvalidPlacementException {
        Board b = new Board();
        b.placeToken(new Token(Token.Type.CERES, 1), 2, 2, Token.Orientation.VERTICAL);
        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 6), 0, 3, Token.Orientation.VERTICAL);

        List<ElementaryTokenMove> moves = new ArrayList();
        moves.add(new ElementaryTokenMove(3, 2));
        moves.add(new ElementaryTokenMove(3, 3));
        moves.add(new ElementaryTokenMove(3, 4));

        try {
            b.executeMoves(2, 2, moves);
        } catch (InvalidMoveException e) {
            assertEquals("the move to 3;3 was illegal", e.getMessage());
        }

        fail("no error was thrown although an illegal move was provided");
    }

    @Test
    public void testLegalMove() throws InvalidPlacementException, InvalidMoveException {
        Board b = new Board();
        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 7), 10, 9, Token.Orientation.HORIZONTAL);
        b.placeToken(new Token(Token.Type.CERES, 1), 10, 10, Token.Orientation.VERTICAL);
        assertEquals('C', b.getTokenAt(10, 10));

        List<ElementaryTokenMove> moves = new ArrayList<>();
        moves.add(new ElementaryTokenMove(9, 9));
        moves.add(new ElementaryTokenMove(9, 8));
        moves.add(new ElementaryTokenMove(10, 8));

        b.executeMoves(9, 10, moves);

        assertEquals('-', b.getTokenAt(10, 10));
        assertEquals('C', b.getTokenAt(10, 8));
    }
}