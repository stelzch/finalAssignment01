package edu.kit.usxim.FinalAssignment1;

public class ElementaryTokenMove extends Coordinates {
    private int dstX;
    private int dstY;

    /**
     * Instantiate a new ElementaryTokenMove
     * @param dstX the x-coordinate to move to
     * @param dstY the y-coordinate to move to
     */
    public ElementaryTokenMove(int dstX, int dstY) {
        super(dstX, dstY);
        this.dstX = dstX;
        this.dstY = dstY;
    }

    /**
     * Copy constructor
     * @param other another token move to copy from
     */
    public ElementaryTokenMove(Coordinates other) {
        super(other.getX(), other.getY());
    }


    /**
     * Checks whether this elementary moves destination neighbors the given one directly (not diagonally)
     * @param other another elementary move
     * @return true if it is in the direct neighbourhood, false otherwise
     */
    public boolean isConnectedTo(ElementaryTokenMove other) {
        int coordinateDistance = Math.abs(other.getX() - getX())
                + Math.abs(other.getY() - getY());
        if (coordinateDistance <= 1)
            return true;

        return false;
    }
}
