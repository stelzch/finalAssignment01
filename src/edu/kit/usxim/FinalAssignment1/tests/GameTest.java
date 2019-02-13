package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Game;
import edu.kit.usxim.FinalAssignment1.InvalidPlacementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

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
        assertEquals(Game.GameState.VC_MOVEMENT_EXPECTED, g.getState());
        g.moveToNextState();
        assertEquals(Game.GameState.DICE_ROLL_EXPECTED, g.getState());
        g.moveToNextState();
        assertEquals(Game.GameState.TOKEN_PLACEMENT_EXPECTED, g.getState());
        g.moveToNextState();
        assertEquals(Game.GameState.VC_MOVEMENT_EXPECTED, g.getState());
    }

    @Test
    public void testBasicVestaPlacement() throws InvalidPlacementException, IllegalAccessException {
        Game g = new Game();
        g.setVC(3, 3);

        assertEquals(Game.GameState.DICE_ROLL_EXPECTED, g.getState());
    }

    @Test
    public void testBasicPlacement() throws InvalidPlacementException, IllegalAccessException, InvalidPlacementException {
        Game g = new Game();
        assertEquals(Game.GameState.VC_MOVEMENT_EXPECTED, g.getState());
        g.setVC(2, 3);
        g.roll("3");
        g.place(0, 0, 2, 0);

        String boardRepr =
                "+++------------\n" +
                "---------------\n" +
                "---V-----------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------";

        assertEquals("V", g.state(2, 3));
        assertEquals("+", g.state(0, 2));
        assertEquals(boardRepr, g.print());
    }

    @Test
    public void testExceptionWhenInvalidPlacement() {
        Executable placeTokenLongerThanRoll = () -> {
            Game g = new Game();

            g.setVC(5, 5);
            g.roll("3");
            g.place(0, 0, 6, 0);
        };

        assertThrows(IllegalArgumentException.class, placeTokenLongerThanRoll);
    }

}