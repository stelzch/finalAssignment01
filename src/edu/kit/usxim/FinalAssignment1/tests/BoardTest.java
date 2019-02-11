package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Board;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {

    @Test
    public void testConstructor() {
        Board b = new Board();

        assertNotNull(b);
    }

    @Test
    public void testGetFieldState() {
        Board b = new Board();

        assertEquals('-', b.getTokenAt(0, 0));
    }

    @Test
    public void testFieldBounds() {
        Executable accessOutOfBounds = () -> {
            Board b = new Board();
            char c = b.getTokenAt(11, 15);
        };
        Executable accessOutOfBoundsNegative = () -> {
            Board b = new Board();
            char c = b.getTokenAt(-1, 2);
        };

        assertThrows(IllegalArgumentException.class, accessOutOfBounds);
    }

}