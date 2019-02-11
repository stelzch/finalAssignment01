package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Token;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;


import java.lang.IllegalArgumentException;

import static edu.kit.usxim.FinalAssignment1.Token.Type.CERES;
import static edu.kit.usxim.FinalAssignment1.Token.Type.MISSION_CONTROL;
import static edu.kit.usxim.FinalAssignment1.Token.Type.VESTA;
import static org.junit.jupiter.api.Assertions.*;

class TokenTest {

    @Test
    public void testStringRepresentation() {
        Token missionControl = new Token(MISSION_CONTROL, 3);
        Token vesta = new Token(Token.Type.VESTA, 1);
        Token ceres = new Token(Token.Type.CERES, 1);

        assertEquals("+", missionControl.toString());
        assertEquals("V", vesta.toString());
        assertEquals("C", ceres.toString());
    }

    @Test
    public void testLowerSizeBoundsMissionControl() {
        Executable negativeTokenSizeConstruction = () -> {
          new Token(MISSION_CONTROL, -1);
        };

        Executable smallTokenSizeConstruction = () -> {
            new Token(MISSION_CONTROL, 1);
        };

        assertThrows(IllegalArgumentException.class, negativeTokenSizeConstruction);
        assertThrows(IllegalArgumentException.class, smallTokenSizeConstruction);
    }

    @Test
    public void testHighSizeBoundsMissionControl() {
        Executable extraLargeTokenSizeConstruction = () -> {
            new Token(MISSION_CONTROL, Integer.MAX_VALUE);
        };

        assertThrows(IllegalArgumentException.class, extraLargeTokenSizeConstruction);
    }

    @Test
    public void testCeresVestaSizeBounds() {
        Executable negativeCeresSizeConstruction = () -> {
            new Token(CERES, Integer.MIN_VALUE);
        };

        Executable largeVestaSizeConstruction = () -> {
            new Token(VESTA, Integer.MAX_VALUE);
        };

        assertThrows(IllegalArgumentException.class, negativeCeresSizeConstruction);
        assertThrows(IllegalArgumentException.class, largeVestaSizeConstruction);
    }

    @Test
    public void testSizeAndTypeGetter() {
        Token t = new Token(MISSION_CONTROL, 3);

        assertEquals(3, t.getSize());
        assertEquals(MISSION_CONTROL, t.getType());
    }


}