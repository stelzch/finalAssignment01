package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Board;
import edu.kit.usxim.FinalAssignment1.FreeFieldCounter;
import edu.kit.usxim.FinalAssignment1.InvalidPlacementException;
import edu.kit.usxim.FinalAssignment1.Token;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FreeFieldCounterTest {

    @Test
    public void testBasicCount() throws InvalidPlacementException {
        Board b = new Board();

        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 6), 0, 3, Token.Orientation.HORIZONTAL);
        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 7), 4, -4, Token.Orientation.VERTICAL);

        assertEquals("----+----------\n" +
                "----+----------\n" +
                "----+----------\n" +
                "++++++---------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------\n" +
                "---------------", b.toString());

        FreeFieldCounter counter = new FreeFieldCounter(b, 0, 1);
        int calculatedFreeFieldNum = counter.countReachableTokens();
        assertEquals(11, calculatedFreeFieldNum);

        System.out.println(b);
        b.placeToken(new Token(Token.Type.MISSION_CONTROL, 2), 0, 2, Token.Orientation.HORIZONTAL);
        counter = new FreeFieldCounter(b, 0, 1);
        calculatedFreeFieldNum = counter.countReachableTokens();
        assertEquals(11 - 2, calculatedFreeFieldNum);
    }

    @Test
    public void testEntrappedVesta() {
        Board b = new Board();
        b.setTokenAt(0, 0, 'V');
        b.setTokenAt(1,1, '+');
        b.setTokenAt(0,1, '+');
        b.setTokenAt(1,0, '+');

        FreeFieldCounter counter = new FreeFieldCounter(b, 0, 0);
        assertEquals(0, counter.countReachableTokens());
    }

    @Test
    public void testSplitGameBoard() {
        Board b = new Board();
        for (int i=0; i<Board.BOARD_WIDTH; i++)
            b.setTokenAt(i, 6, '+');

        FreeFieldCounter counter = new FreeFieldCounter(b, 5, 2);
        assertEquals(6 * 15 - 1, counter.countReachableTokens());
    }

}