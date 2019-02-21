package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import edu.kit.usxim.FinalAssignment1.exceptions.*;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class NatureTokenSetTest {

    @Test
    public void basicPlacementRoutines() throws InvalidPlacementException, InvalidCoordinatesException {
        Board b = new Board();
        NatureTokenSet nts = new NatureTokenSet(b);

        nts.placeVC(Game.GamePhase.PHASE_ONE, new Coordinates(2, 3));
        nts.placeVC(Game.GamePhase.PHASE_TWO, new Coordinates(2, 4));

        assertEquals('V', b.getTokenAt(new Coordinates(2, 3)));
        assertEquals('C', b.getTokenAt(new Coordinates(2, 4)));
    }


    @Test
    public void testMultipleVestaPlacement() throws InvalidPlacementException, InvalidCoordinatesException {
        Board b = new Board();
        NatureTokenSet nts = new NatureTokenSet(b);

        nts.placeVC(Game.GamePhase.PHASE_ONE, new Coordinates(0, 0));
        try {
            nts.placeVC(Game.GamePhase.PHASE_ONE, new Coordinates(0, 3));
        } catch (InvalidPlacementException e) {
            assertEquals("vesta has already been placed", e.getMessage());
            return;
        }

        fail("no error was thrown");
    }

    @Test
    public void testBasicElementaryMoves() throws InvalidPlacementException, InvalidMoveException, InvalidCoordinatesException {
        Board b = new Board();
        NatureTokenSet nts = new NatureTokenSet(b);
        nts.placeVC(Game.GamePhase.PHASE_TWO, new Coordinates(5, 4));
        assertEquals('C', b.getTokenAt(new Coordinates(5, 4)));

        List<ElementaryTokenMove> moves = new ArrayList<>();
        moves.add(new ElementaryTokenMove(5, 5));

        nts.moveVC(Game.GamePhase.PHASE_TWO, moves);

        assertEquals('C', b.getTokenAt(new Coordinates(5, 5)));
    }

    @Test
    public void testErrorOnIllegalMove() {
        Executable performIllegalMove = () -> {
            Board b = new Board();
            NatureTokenSet nts = new NatureTokenSet(b);
            nts.placeVC(Game.GamePhase.PHASE_TWO, new Coordinates(5, 4));

            List<ElementaryTokenMove> moves = new ArrayList<>();

            moves.add(new ElementaryTokenMove(9, 9));

            nts.countStepsAndCheckIfLegal(Game.GamePhase.PHASE_TWO, moves);
        };

        assertThrows(InvalidMoveException.class, performIllegalMove);
    }

    @Test
    public void testIllegalMoveThroughOtherToken() throws InvalidPlacementException, InvalidCoordinatesException {
        Board b = new Board();
        NatureTokenSet nts = new NatureTokenSet(b);

        nts.placeVC(Game.GamePhase.PHASE_TWO, new Coordinates(2, 2));
        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 6), new Coordinates(0, 3), Token.Orientation.HORIZONTAL);

        assertEquals("---------------\n" +
                "---------------\n" +
                "--C------------\n" +
                "++++++---------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------", b.toString());

        List<ElementaryTokenMove> moves = new ArrayList();
        moves.add(new ElementaryTokenMove(3, 2));
        moves.add(new ElementaryTokenMove(3, 3));
        moves.add(new ElementaryTokenMove(3, 4));

        try {
            nts.countStepsAndCheckIfLegal(Game.GamePhase.PHASE_TWO, moves);
        } catch (InvalidMoveException e) {
            assertEquals("cant move through 3;3 - field is occupied", e.getMessage());
            return;
        }

        fail("no error was thrown although an illegal move was provided");
    }

    @Test
    public void testLegalMove() throws InvalidPlacementException, InvalidMoveException, InvalidCoordinatesException {
        Board b = new Board();
        NatureTokenSet nts = new NatureTokenSet(b);

        nts.placeVC(Game.GamePhase.PHASE_TWO, new Coordinates(10, 10));
        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 7), new Coordinates(10, 9), Token.Orientation.HORIZONTAL);
        assertEquals('C', b.getTokenAt(new Coordinates(10, 10)));
        assertEquals('-', b.getTokenAt(new Coordinates(9, 9)));
        assertEquals("---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "----------+++++\n" +
                "----------C----", b.toString());

        List<ElementaryTokenMove> moves = new ArrayList<>();
        moves.add(new ElementaryTokenMove(9, 10));
        moves.add(new ElementaryTokenMove(9, 9));
        moves.add(new ElementaryTokenMove(9, 8));
        moves.add(new ElementaryTokenMove(10, 8));


        assertEquals(4, nts.countStepsAndCheckIfLegal(Game.GamePhase.PHASE_TWO, moves));
        nts.moveVC(Game.GamePhase.PHASE_TWO, moves);

        assertEquals('-', b.getTokenAt(new Coordinates(10, 10)));
        assertEquals('C', b.getTokenAt(new Coordinates(10, 8)));
    }

    @Test
    public void testReachableFieldsCalc() throws InvalidPlacementException, InvalidCoordinatesException {
        Board b = new Board();
        NatureTokenSet nts = new NatureTokenSet(b);
        nts.placeVC(Game.GamePhase.PHASE_ONE, new Coordinates(5, 5));

        assertEquals(Board.BOARD_WIDTH * Board.BOARD_HEIGHT - 1,
                nts.getNumOfReachableFields(Game.GamePhase.PHASE_ONE));
    }
}