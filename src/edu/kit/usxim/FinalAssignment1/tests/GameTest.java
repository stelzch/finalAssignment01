package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Game;
import edu.kit.usxim.FinalAssignment1.InvalidPlacementException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    /**
     * Assert that the game looks like the string given in repr
     * @param repr the expected game board representation as string
     * @param g the game
     */
    private void assertGameBoardState(String repr, Game g) {
        assertEquals(repr, g.print());
    }

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

        assertGameBoardState(
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
                "---------------", g);

        assertEquals("V", g.state(2, 3));
        assertEquals("+", g.state(0, 2));
    }

    @Test
    public void testDawnPlacement() throws InvalidPlacementException, IllegalAccessException {
        Game g = new Game();
        g.setVC(5, 2);
        g.roll("DAWN");

        g.place(-5, 0, 1, 0);

        assertGameBoardState(
                "++-------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "--V------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------", g);

        /* Try another game */
        g = new Game();
        g.setVC(9, 14);
        g.roll("DAWN");
        g.place(19, 10, 13, 10);

        assertGameBoardState(
                "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "--------------V\n" +
                        "-------------++", g);
    }


    @Test
    public void testExceptionWhenInvalidPlacement() {
        Executable placeTokenLongerThanRoll = () -> {
            Game g = new Game();

            g.setVC(5, 5);
            g.roll("3");
            g.place(0, 0, 6, 0);
        };

        Executable placeTokenFittingRoll = () -> {
            Game g = new Game();

            g.setVC(5, 5);
            g.roll("3");
            g.place(0, 0, 2, 0);
        };

        assertThrows(IllegalArgumentException.class, placeTokenLongerThanRoll);
        assertDoesNotThrow(placeTokenFittingRoll);
    }

    @Test
    public void testIllegalVestaMovement() {
        Executable moveVestaTooLong = () -> {
            Game g = new Game();
            g.setVC(4, 4);
            g.roll("DAWN");
            g.place(0, 0, 6, 0);
            g.setVC(8, 9); // moved much further than 7 steps
        };

        Executable moveVestaThroughMC = () -> {
            Game g = new Game();
            g.setVC(5, 5);
            g.roll("DAWN");
        };

        assertThrows(IllegalArgumentException.class, moveVestaTooLong);
    }

}