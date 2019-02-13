package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Token;
import edu.kit.usxim.FinalAssignment1.Utils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.*;

class UtilsTest {

    @Test
    public void testBasicLineOrientation() {
        assertEquals(Token.Orientation.VERTICAL, Utils.getLineOrientation(2, 2, 2, 9));
        assertEquals(Token.Orientation.HORIZONTAL, Utils.getLineOrientation(-60, Integer.MAX_VALUE, -60000, Integer.MAX_VALUE));
    }

    @Test
    public void testBackwardsLineOrientation() {
        Token.Orientation expected = Token.Orientation.HORIZONTAL;
        Token.Orientation actual = Utils.getLineOrientation(4, 4, -40, 4);

        assertEquals(expected, actual);
    }

    @Test
    public void testErrorOnNonStraightLines() {
        Executable getOrientationOfTiltedLine = () -> {
            Token.Orientation or;
            or = Utils.getLineOrientation(2, 2, 6, 6);
        };

        assertThrows(IllegalArgumentException.class, getOrientationOfTiltedLine);
    }

}