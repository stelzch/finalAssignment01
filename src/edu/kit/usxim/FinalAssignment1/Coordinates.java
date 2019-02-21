package edu.kit.usxim.FinalAssignment1;

public class Coordinates extends Object {
    protected int x;
    protected int y;

    /**
     * Create a  new Coordinates object
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @return the x-coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * @return the y-coordinate
     */
    public int getY() {
        return y;
    }

    /**
     * @param o the object to test for equality with
     * @return true if the coordinates are the same
     */
    @Override
    public boolean equals(Object o) {
        if (o instanceof Coordinates) {
            Coordinates other = (Coordinates) o;
            return (other.getX() == getX()) && (other.getY() == getY());
        }

        throw new IllegalArgumentException("objects to compare must both be of type Coordinates");
    }
}
