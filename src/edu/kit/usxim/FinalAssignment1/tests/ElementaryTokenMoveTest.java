package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Coordinates;
import edu.kit.usxim.FinalAssignment1.ElementaryTokenMove;
import org.junit.jupiter.api.Test;

import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class ElementaryTokenMoveTest {

    @Test
    public void testStringRepresentation() {
        ElementaryTokenMove move = new ElementaryTokenMove(555, 2343);
        assertEquals("2343;555", move.toString());
    }

    @Test
    public void testEquality() {
        ElementaryTokenMove move1 = new ElementaryTokenMove(4, 5);
        ElementaryTokenMove move2 = new ElementaryTokenMove(5, 5);
        ElementaryTokenMove move3 = new ElementaryTokenMove(4, 5);

        assertTrue(move1.equals(move3));
        assertFalse(move1.equals(move2));
    }

    @Test
    public void testNeighbours() {
        ElementaryTokenMove move1 = new ElementaryTokenMove(4, 5);
        ElementaryTokenMove move2 = new ElementaryTokenMove(5, 5);
        ElementaryTokenMove move3 = new ElementaryTokenMove(5, 6);

        assertTrue(move1.isConnectedTo(move2));
        assertFalse(move1.isConnectedTo(move3));
        assertTrue(move2.isConnectedTo(move3));
    }

    @Test
    public void testReflexivity() {
        for (int i = 0; i < 8; i++) {
            Random rnd = new Random();

            int x = Math.abs(rnd.nextInt());
            int y = Math.abs(rnd.nextInt());
            ElementaryTokenMove move = new ElementaryTokenMove(x, y);
            ElementaryTokenMove move2 = new ElementaryTokenMove(x, y);

            assertTrue(move.isConnectedTo(move2));
        }
    }

    @Test
    public void testSymmetry() {
        for (int i = 0; i < 500; i++) {
            Random rnd = new Random();

            int x1 = Math.abs(rnd.nextInt());
            int x2 = Math.abs(rnd.nextInt());
            int y1 = Math.abs(rnd.nextInt());
            int y2 = Math.abs(rnd.nextInt());

            ElementaryTokenMove move1 = new ElementaryTokenMove(x1, y1);
            ElementaryTokenMove move2 = new ElementaryTokenMove(x2, y2);

            assertEquals(move1.isConnectedTo(move2), move2.isConnectedTo(move1));
        }
    }

    @Test
    public void testDiagonalMoves() {
        ElementaryTokenMove center = new ElementaryTokenMove(7, 5);
        ElementaryTokenMove upperLeft = new ElementaryTokenMove(6, 4);
        ElementaryTokenMove upperRight = new ElementaryTokenMove(8, 4);
        ElementaryTokenMove lowerLeft = new ElementaryTokenMove(6, 6);
        ElementaryTokenMove lowerRight = new ElementaryTokenMove(8, 6);

        assertFalse(upperLeft.isConnectedTo(center));
        assertFalse(upperRight.isConnectedTo(center));
        assertFalse(lowerLeft.isConnectedTo(center));
        assertFalse(lowerRight.isConnectedTo(center));
    }

    @Test
    public void testEqualsToCoordinates() {
        ElementaryTokenMove movePos = new ElementaryTokenMove(2, 3);
        Coordinates coordPos = new Coordinates(2, 3);

        assertTrue(coordPos.equals(movePos));
    }
}