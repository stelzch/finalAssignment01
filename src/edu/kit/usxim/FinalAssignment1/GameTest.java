package edu.kit.usxim.FinalAssignment1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    @Test
    public void testInitialGameState() {
        Game g = new Game();
        assertEquals('-', g.state(0, 0).charAt(0));
    }

    @Test
    public void testStateTransition() {
        Game g = new Game();
        assertEquals(Game.RoundState.VC_MOVEMENT_EXPECTED, g.getState());
        g.moveToNextState();
        assertEquals(Game.RoundState.DICE_ROLL_EXPECTED, g.getState());
        g.moveToNextState();
        assertEquals(Game.RoundState.TOKEN_PLACEMENT_EXPECTED, g.getState());
        g.moveToNextState();
        assertEquals(Game.RoundState.VC_MOVEMENT_EXPECTED, g.getState());
    }

    @Test
    public void testBasicPlacement() throws InvalidPlacementException, IllegalAccessException {
        Game g = new Game();
        assertEquals(Game.RoundState.VC_MOVEMENT_EXPECTED, g.getState());
        g.setVC(2, 3);
        g.roll("3");
        g.place(0, 0, 2, 0);

        String boardRepr =
                "+++------------\n" +
                "---------------\n" +
                "---------------\n" +
                "--V------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n";

        assertEquals("V", g.state(2, 3));
        assertEquals("+", g.state(0, 2));
        assertEquals(boardRepr, g.print());
    }

}