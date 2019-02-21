package edu.kit.usxim.FinalAssignment1;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordinatesTest {

    @Test
    public void testBasicSetup() {
        Coordinates coords = new Coordinates(4, 3);

        assertEquals(4, coords.getX());
        assertEquals(3, coords.getY());
    }

    @Test
    public void testEquals() {
        Coordinates coord1 = new Coordinates(2, 2);
        Coordinates coord2 = new Coordinates(2, 2);
        Coordinates coord3 = new Coordinates(4, 2);

        assertTrue(coord1.equals(coord2));
        assertTrue(coord2.equals(coord1));

        assertFalse(coord1.equals(coord3));
    }

}