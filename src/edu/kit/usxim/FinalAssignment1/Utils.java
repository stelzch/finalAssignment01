package edu.kit.usxim.FinalAssignment1;

public class Utils {

    private static void throwErrorIfLineNotStraight(Coordinates start, Coordinates end) {
        if (start.getX() != end.getX() && start.getY() != end.getY()) {
            throw new IllegalArgumentException("the given coordinates are not in a straight line");
        }
    }

    /**
     * Check whether the specified line is straight, thus either
     * @param start the starting coordinates
     * @param end the ending coordiantes
     * @return the orientation of the line
     */
    public static Token.Orientation getLineOrientation(Coordinates start, Coordinates end) {
        throwErrorIfLineNotStraight(start, end);

        if (start.getX() == end.getX())
            return Token.Orientation.VERTICAL;

        if (start.getY() == end.getY())
            return Token.Orientation.HORIZONTAL;

        throw new IllegalArgumentException("the given coordinates are weird!");
    }

    /**
     * Get the length of a line (including the ending and starting points)
     * @param start the starting coordinates
     * @param end the ending coordinates
     * @return the length of the line
     */
    public static int getStraightLineLength(Coordinates start, Coordinates end) {
        throwErrorIfLineNotStraight(start, end);

        Token.Orientation or = getLineOrientation(start, end);

        switch (or) {
            case HORIZONTAL:
                return Math.abs(start.getX() - end.getX()) + 1;
            case VERTICAL:
                return Math.abs(start.getY() - end.getY()) + 1;
            default:
                return 0;
        }
    }
}
