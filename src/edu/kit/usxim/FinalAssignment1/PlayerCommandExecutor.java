package edu.kit.usxim.FinalAssignment1;

/**
 * This interface specifies all the commands the user can enter
 */
public interface PlayerCommandExecutor {
    /**
     * Get the board game state at a specific field
     * @param m the y-coordinate
     * @param n the x-coordinate
     * @return the state at the given position
     */
    String state(int m, int n);

    /**
     * Get the board as string representation
     * @return ASCII representation of the game board
     */
    String print();

    /**
     * Set the vesta or ceres tokens on new positions
     * @param m the y-coordinate
     * @param n the x-coordinate
     * @return status message
     */
    String setVC(int m, int n);

    /**
     * Signals that the dice has been rolled
     * @param symbol the symbol the dice shows
     * @return status message
     */
    String roll(String symbol);

    /**
     * Place a mission-control token on a field
     * @param x1 the starting x-coordinate
     * @param y1 the starting y-coordinate
     * @param x2 the ending x-coordinate
     * @param y2 the ending x-coordinate
     * @return status message
     */
    String place(int x1, int y1, int x2, int y2);
}