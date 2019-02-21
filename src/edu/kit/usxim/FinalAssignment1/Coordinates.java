package edu.kit.usxim.FinalAssignment1;

public class Coordinates extends Object {
    /** The x-coordinate */
    protected int x;
    /** The y-coordinate */
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
     * @param x the new x-coordinate
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @param y the new y-coordinate
     */
    public void setY(int y) {
        this.y = y;
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

    /**
     * @return the string representation of the elementary move x;y
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getY());
        sb.append(";");
        sb.append(getX());

        return sb.toString();
    }
}
