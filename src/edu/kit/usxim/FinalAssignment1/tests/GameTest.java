package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Coordinates;
import edu.kit.usxim.FinalAssignment1.ElementaryTokenMove;
import edu.kit.usxim.FinalAssignment1.Game;
import edu.kit.usxim.FinalAssignment1.exceptions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {

    /**
     * Assert that the game looks like the string given in repr
     *
     * @param repr the expected game board representation as string
     * @param g    the game
     */
    private void assertGameBoardState(String repr, Game g) {
        assertEquals(repr, g.print());
    }

    @Test
    public void testInitialGameState() throws InvalidCoordinatesException {
        Game g = new Game();
        assertEquals('-', g.state(new Coordinates(0, 0)).charAt(0));
    }

    @Test
    public void testStateTransition() throws GameException {
        Game g = new Game();

        assertEquals(Game.GameState.VC_PLACEMENT_EXPECTED, g.getState());

        g.setVC(new Coordinates(0, 0));

        for (int i = 1; i <= 6; i++) {
            System.out.println("Round " + i);
            assertEquals(Game.GameState.DICE_ROLL_EXPECTED, g.getState());
            g.moveToNextState();
            assertEquals(Game.GameState.TOKEN_PLACEMENT_EXPECTED, g.getState());
            g.moveToNextState();
            assertEquals(Game.GameState.VC_MOVEMENT_EXPECTED, g.getState());
            g.moveToNextState();
        }

        assertEquals(Game.GameState.VC_PLACEMENT_EXPECTED, g.getState());
        g.setVC(new Coordinates(2, 2));

        for (int i = 1; i <= 6; i++) {
            System.out.println("Round " + i);
            assertEquals(Game.GameState.DICE_ROLL_EXPECTED, g.getState());
            g.moveToNextState();
            assertEquals(Game.GameState.TOKEN_PLACEMENT_EXPECTED, g.getState());
            g.moveToNextState();
            assertEquals(Game.GameState.VC_MOVEMENT_EXPECTED, g.getState());
            g.moveToNextState();
        }
        assertEquals(Game.GameState.GAME_FINISHED, g.getState());

    }

    @Test
    public void testBasicVestaPlacement() throws GameException {
        Game g = new Game();
        g.setVC(new Coordinates(3, 3));

        assertEquals(Game.GameState.DICE_ROLL_EXPECTED, g.getState());
    }

    @Test
    public void testBasicPlacement() throws GameException {
        Game g = new Game();
        assertEquals(Game.GameState.VC_PLACEMENT_EXPECTED, g.getState());
        g.setVC(new Coordinates(3, 2));
        g.roll("3");
        g.place(new Coordinates(0, 0), new Coordinates(2, 0));

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

        assertEquals("V", g.state(new Coordinates(3, 2)));
        assertEquals("+", g.state(new Coordinates(2, 0)));
    }

    @Test
    public void testDawnPlacement() throws GameException {
        Game g = new Game();
        g.setVC(new Coordinates(2, 5));
        g.roll("DAWN");

        g.place(new Coordinates(-5, 0), new Coordinates(1, 0));

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
        g.setVC(new Coordinates(14, 9));
        g.roll("DAWN");
        g.place(new Coordinates(19, 10), new Coordinates(13, 10));

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

            g.setVC(new Coordinates(5, 5));
            g.roll("3");
            g.place(new Coordinates(0, 0), new Coordinates(6, 0));
        };

        Executable placeTokenFittingRoll = () -> {
            Game g = new Game();

            g.setVC(new Coordinates(5, 5));
            g.roll("3");
            g.place(new Coordinates(0, 0), new Coordinates(2, 0));
        };

        assertThrows(InvalidPlacementException.class, placeTokenLongerThanRoll);
        assertDoesNotThrow(placeTokenFittingRoll);
    }

    @Test
    public void testIllegalVestaMovement() {
        Executable moveVestaTooLong = () -> {
            Game g = new Game();
            g.setVC(new Coordinates(4, 4));
            g.roll("DAWN");
            g.place(new Coordinates(0, 0), new Coordinates(6, 0));

            List<ElementaryTokenMove> moves = new ArrayList<ElementaryTokenMove>();
            moves.add(new ElementaryTokenMove(5, 4));
            moves.add(new ElementaryTokenMove(5, 5));
            moves.add(new ElementaryTokenMove(5, 6));
            moves.add(new ElementaryTokenMove(6, 6));
            moves.add(new ElementaryTokenMove(7, 6));
            moves.add(new ElementaryTokenMove(7, 7));
            moves.add(new ElementaryTokenMove(7, 8));

            // This step is not allowed
            moves.add(new ElementaryTokenMove(7, 9));

            g.move(moves);
        };

        Executable moveVestaThroughMC = () -> {
            Game g = new Game();
            g.setVC(new Coordinates(5, 5));
            g.roll("DAWN");
        };

        assertThrows(InvalidMoveException.class, moveVestaTooLong);
    }

    @Test
    public void testSimpleMovement() throws GameException {
        Game g = new Game();
        List<ElementaryTokenMove> moveOneStepForward = new ArrayList<>();
        List<ElementaryTokenMove> moveOneStepBack = new ArrayList<>();

        moveOneStepForward.add(new ElementaryTokenMove(1, 0));
        moveOneStepBack.add(new ElementaryTokenMove(0, 0));

        g.setVC(new Coordinates(0, 0));
        g.roll("2");
        g.place(new Coordinates(5, 0), new Coordinates(5, 1));
        g.move(moveOneStepForward);

        assertEquals("V", g.state(new Coordinates(1, 0)));

        g.roll("2");
        g.place(new Coordinates(6, 0), new Coordinates(6, 2));
        g.move(moveOneStepBack);

        System.out.println("Gamestate: " + g.print());
        assertEquals("V", g.state(new Coordinates(0, 0)));
    }

    @Test
    public void testMultiplePhases() throws GameException {
        Game g = new Game();
        List<ElementaryTokenMove> moveOneStepForward = new ArrayList<>();
        List<ElementaryTokenMove> moveOneStepBack = new ArrayList<>();
        List<ElementaryTokenMove> moveToThirdPos = new ArrayList<>();

        moveOneStepForward.add(new ElementaryTokenMove(1, 0));
        moveOneStepBack.add(new ElementaryTokenMove(0, 0));
        moveToThirdPos.add(new ElementaryTokenMove(2, 0));


        // Round 1
        g.setVC(new Coordinates(0, 0));
        for (int round = 2; round <= 7; round++) {
            g.roll((round == 7) ? "DAWN" : String.valueOf(round));
            g.place(new Coordinates(0, round), new Coordinates(round - 1, round));

            g.move((round % 2 == 0) ? moveOneStepForward : moveOneStepBack);
        }
        assertGameBoardState(
                "V--------------\n" +
                        "---------------\n" +
                        "++-------------\n" +
                        "+++------------\n" +
                        "++++-----------\n" +
                        "+++++----------\n" +
                        "++++++---------\n" +
                        "+++++++--------\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------", g);

        // Round 2
        g.setVC(new Coordinates(1, 0));
        for (int round = 2; round <= 7; round++) {
            g.roll((round == 7) ? "DAWN" : String.valueOf(round));
            g.place(new Coordinates(14, round), new Coordinates(14 - (round - 1), round));

            g.move((round % 2 == 0) ? moveToThirdPos : moveOneStepForward);
        }
        assertGameBoardState(
                "VC-------------\n" +
                        "---------------\n" +
                        "++-----------++\n" +
                        "+++---------+++\n" +
                        "++++-------++++\n" +
                        "+++++-----+++++\n" +
                        "++++++---++++++\n" +
                        "+++++++-+++++++\n" +
                        "---------------\n" +
                        "---------------\n" +
                        "---------------", g);

        //fail("This test needs to be written");
    }

    @Test
    public void testVestaMovementMinimum() throws GameException {
        Executable moveWithEmptyList = () -> {
            Game g = new Game();
            g.setVC(new Coordinates(0, 0));

            g.roll("DAWN");
            g.place(new Coordinates(1, 0), new Coordinates(1 + 6, 0));
            g.move(new ArrayList<ElementaryTokenMove>());
        };

        Executable moveWithSameCoordsList = () -> {
            Game g = new Game();
            g.setVC(new Coordinates(0, 0));

            g.roll("DAWN");
            g.place(new Coordinates(1, 0), new Coordinates(1 + 6, 0));

            List<ElementaryTokenMove> moves = new ArrayList<>();
            moves.add(new ElementaryTokenMove(0, 0));
            moves.add(new ElementaryTokenMove(0, 0));
            g.move(moves);
            System.out.println(g.print());
        };

        assertThrows(InvalidMoveException.class, moveWithEmptyList);
        assertThrows(InvalidMoveException.class, moveWithSameCoordsList);
    }

    private List<ElementaryTokenMove> getListForSingleMove(ElementaryTokenMove move) {
        List<ElementaryTokenMove> list = new ArrayList<>();
        list.add(move);
        return list;
    }

    @Test
    public void testImmovableVesta() throws GameException {
        Game g = new Game();
        g.setVC(new Coordinates(0, 1));
        g.roll("2");
        g.place(new Coordinates(1, 0), new Coordinates(2, 0));
        g.move(getListForSingleMove(new ElementaryTokenMove(0, 0)));
        assertEquals("V", g.state(new Coordinates(0, 0)));

        g.roll("2");
        g.place(new Coordinates(0, 1), new Coordinates(0, 3));
        System.out.println(g.print());

        try {
            g.roll("2");
        } catch (GameException e) {
            fail("as vesta can not be moved the vesta movement step should be skipped");
        }
    }

    @Test
    public void testIllegalDiceSymbols() {
        Executable sevenInsteadOfDawn = () -> {
            Game g = new Game();
            g.setVC(new Coordinates(0, 0));

            g.roll("7");
        };

        Executable glibberishDiceNumber = () -> {
            Game g = new Game();
            g.setVC(new Coordinates(0, 0));

            g.roll("5 53");
        };

        assertThrows(InvalidDiceNumberException.class, sevenInsteadOfDawn);
        assertThrows(InvalidDiceNumberException.class, glibberishDiceNumber);
    }

    @Test
    public void testResultOfUnfinishedGame() {
        Executable invalidStateForResultRequest = () -> {
            Game g = new Game();
            int res = g.result();
        };

        assertThrows(InvalidCommandException.class, invalidStateForResultRequest);
    }
}