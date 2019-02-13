package edu.kit.usxim.FinalAssignment1;

public class Utils {

    private static void throwErrorIfLineNotStraight(int x1, int y1, int x2, int y2) {
        if (x1 != x2 && y1 != y2) {
            throw new IllegalArgumentException("the given coordinates are not in a straight line");
        }
    }

    /**
     * Check whether the specified line is straight, thus either
     * @param x1 starting x
     * @param y1 starting y
     * @param x2 end x
     * @param y2 end y
     * @return the orientation of the line
     */
    public static Token.Orientation getLineOrientation(int x1, int y1, int x2, int y2) {
        throwErrorIfLineNotStraight(x1, y1, x2, y2);

        if (x1 == x2)
            return Token.Orientation.VERTICAL;

        if (y1 == y2)
            return Token.Orientation.HORIZONTAL;

        throw new IllegalArgumentException("the given coordinates are weird!");

    }
}
