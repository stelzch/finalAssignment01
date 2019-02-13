package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import static org.junit.jupiter.api.Assertions.*;

class NatureTokenSetTest {

    @Test
    public void basicPlacementRoutines() {
        Board b = new Board();
        NatureTokenSet nts = new NatureTokenSet(b);

        nts.placeVC(Game.GamePhase.PHASE_ONE, 2, 3);
        nts.placeVC(Game.GamePhase.PHASE_TWO, 2, 4);

        assertEquals('V', b.getTokenAt(2, 3));
        assertEquals('C', b.getTokenAt(2, 4));
    }

    @Test
    public void alreadyOccupiedException() {
        Executable createOverlappingSituation = () ->  {
            Board b = new Board();
            Token mcSix = new Token(Token.Type.MISSION_CONTROL, 6);
            b.placeToken(mcSix, 2, 2, Token.Orientation.VERTICAL);

            NatureTokenSet nts = new NatureTokenSet(b);

            nts.placeVC(Game.GamePhase.PHASE_ONE, 2, 5);
        };

        assertThrows(InvalidPlacementException.class, createOverlappingSituation);
    }

    @Test
    public void testReplacmentOfVesta() {
        Board b = new Board();
        NatureTokenSet nts = new NatureTokenSet(b);

        nts.placeVC(Game.GamePhase.PHASE_ONE, 14, 10);
        nts.placeVC(Game.GamePhase.PHASE_ONE, 2, 2);

        assertEquals('V', b.getTokenAt(2, 2));
        assertEquals('-', b.getTokenAt(14, 10));
    }

    @Test
    public void testInvalidMovementOfCeres() throws InvalidPlacementException {
        Board b = new Board();
        NatureTokenSet nts = new NatureTokenSet(b);
        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 4), 6, 6, Token.Orientation.VERTICAL);

        nts.placeVC(Game.GamePhase.PHASE_ONE, 5, 6);
        try {
            nts.placeVC(Game.GamePhase.PHASE_ONE, 6, 7);
        } catch (InvalidPlacementException e) {

        }

        assertEquals('+', b.getTokenAt(6, 7));
        assertEquals('V', b.getTokenAt(5, 6));
    }

}