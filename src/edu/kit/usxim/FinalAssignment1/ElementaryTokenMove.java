package edu.kit.usxim.FinalAssignment1;

public class ElementaryTokenMove {
    private int dstX;
    private int dstY;

    /**
     * Instantiate a new ElementaryTokenMove
     * @param dstX the x-coordinate to move to
     * @param dstY the y-coordinate to move to
     */
    public ElementaryTokenMove(int dstX, int dstY) {
        this.dstX = dstX;
        this.dstY = dstY;
    }

    /**
     * @return the destination x coordinate of the move
     */
    public int getDstX() {
        return dstX;
    }

    /**
     * @return the destination y coordinate of the move
     */
    public int getDstY() {
        return dstY;
    }

    /**
     * @return the string representation of the elementary move x;y
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dstY);
        sb.append(";");
        sb.append(dstX);

        return sb.toString();
    }
}
