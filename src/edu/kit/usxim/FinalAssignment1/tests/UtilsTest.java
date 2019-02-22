package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.Coordinates;
import edu.kit.usxim.FinalAssignment1.Token;
import edu.kit.usxim.FinalAssignment1.Utils;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class UtilsTest {

    @Test
    public void testBasicLineOrientation() throws InvalidCoordinatesException {
        assertEquals(Token.Orientation.VERTICAL,
                Utils.getLineOrientation(new Coordinates(2, 2), new Coordinates(2, 9)));
        assertEquals(Token.Orientation.HORIZONTAL,
                Utils.getLineOrientation(
                        new Coordinates(-60, Integer.MAX_VALUE),
                        new Coordinates(-60000, Integer.MAX_VALUE)));
    }

    @Test
    public void testBackwardsLineOrientation() throws InvalidCoordinatesException {
        Token.Orientation expected = Token.Orientation.HORIZONTAL;
        Token.Orientation actual = Utils.getLineOrientation(new Coordinates(4, 4), new Coordinates(-40, 4));

        assertEquals(expected, actual);
    }

    @Test
    public void testErrorOnNonStraightLines() {
        Executable getOrientationOfTiltedLine = () -> {
            Token.Orientation or;
            or = Utils.getLineOrientation(new Coordinates(2, 2), new Coordinates(6, 6));
        };

        assertThrows(InvalidCoordinatesException.class, getOrientationOfTiltedLine);
    }

    @Test
    public void testLineLengths() throws InvalidCoordinatesException {
        assertEquals(7, Utils.getStraightLineLength(new Coordinates(4, 3), new Coordinates(4, 9)));

        assertEquals(901, Utils.getStraightLineLength(new Coordinates(0, 0),
                new Coordinates(0, 900)));

        assertEquals(3, Utils.getStraightLineLength(new Coordinates(3, 3), new Coordinates(3, 1)));
    }

}