package edu.kit.usxim.FinalAssignment1;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import static org.junit.jupiter.api.Assertions.*;

class NatureTokenSetTest {

    @Test
    public void testCreation() {
        NatureTokenSet nts = new NatureTokenSet();

    }

    @Test
    public void testPositionRetrievalException() {
        Executable getPositionBeforeBeingSet = () -> {
            NatureTokenSet nts = new NatureTokenSet();

            int curX = nts.getCurrentX(Game.Phase.PHASE_ONE);
            int curY = nts.getCurrentY(Game.Phase.PHASE_ONE);
        };

        Executable getPositionAfterBeingSet = () -> {
            NatureTokenSet nts = new NatureTokenSet();

            nts.setCurrentX(3, Game.Phase.PHASE_ONE);
            nts.setCurrentY(1, Game.Phase.PHASE_ONE);

            assertEquals(3, nts.getCurrentX(Game.Phase.PHASE_ONE));
            assertEquals(1, nts.getCurrentY(Game.Phase.PHASE_ONE));

        };

        assertThrows(IllegalAccessException.class, getPositionBeforeBeingSet);
        assertDoesNotThrow(getPositionAfterBeingSet);
    }

    @Test
    public void testSeperationBetweenGamePhases() throws IllegalAccessException {
        NatureTokenSet nts = new NatureTokenSet();

        nts.setCurrentX(2, Game.Phase.PHASE_ONE);
        nts.setCurrentY(2, Game.Phase.PHASE_ONE);

        nts.setCurrentX(4, Game.Phase.PHASE_TWO);
        nts.setCurrentY(1, Game.Phase.PHASE_TWO);

        assertEquals(4, nts.getCurrentX(Game.Phase.PHASE_TWO));
        assertEquals(2, nts.getCurrentX(Game.Phase.PHASE_ONE));
    }

}