package edu.kit.usxim.FinalAssignment1;

/**
 * The board stores the current game state
 */
public class Board {
    private static final int BOARD_WIDTH = 15;
    private static final int BOARD_HEIGHT = 11;

    private char[][] board;

    /**
     * Default constructor
     */
    public Board() {
        board = new char[BOARD_HEIGHT][BOARD_WIDTH];

        setFieldsToDefault('-');
    }

    private void setFieldsToDefault(char defaultValue) {
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for ( int y = 0; y < BOARD_HEIGHT; y++) {
                board[y][x] = defaultValue;
            }
        }
    }

    /**
     * Get the field state
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return the character representation at the given coordinates
     */
    public char getTokenAt(int x, int y) {
        throwErrorForInvalidCoords(x, y);
        return board[y][x];
    }


    private void throwErrorForInvalidCoords(int x, int y) {
        if (x < 0 || y < 0)
            throw new IllegalArgumentException("coordinates must be positive integers!");

        if (x > (BOARD_WIDTH - 1))
            throw new IllegalArgumentException("x coordinate must be smaller than " + BOARD_WIDTH);

        if (y > (BOARD_HEIGHT - 1))
            throw new IllegalArgumentException("y coordinate must be bigger than " + BOARD_HEIGHT);
    }
}
