package edu.kit.usxim.FinalAssignment1;

import java.util.Collection;

/**
 * The board stores the current game state
 */
public class Board {
    private static final int BOARD_WIDTH = 15;
    private static final int BOARD_HEIGHT = 11;
    private static final char UNOCCUPIED_FIELD = '-';

    private char[][] board;

    /**
     * Default constructor
     */
    public Board() {
        board = new char[BOARD_HEIGHT][BOARD_WIDTH];

        setFieldsToDefault(UNOCCUPIED_FIELD);
    }

    private void setFieldsToDefault(char defaultValue) {
        for (int x = 0; x < BOARD_WIDTH; x++) {
            for (int y = 0; y < BOARD_HEIGHT; y++) {
                board[y][x] = defaultValue;
            }
        }
    }

    /**
     * Get the field state
     *
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

    private void throwErrorForInvalidPlacement(int x, int y, int endX, int endY) throws InvalidPlacementException {
        try {
            throwErrorForInvalidCoords(x, y);
            throwErrorForInvalidCoords(endX, endY);
        } catch (IllegalArgumentException e) {
            // The coordinates are wrong, throw illegal placement exc
            throw new InvalidPlacementException("the coordinates are out of bounds");
        }

        if ((x > endX) || (y > endY)) {
            throw new InvalidPlacementException("the starting coordinates must be smaller");
        }
    }

    private void throwErrorForInvalidDawnPlacement(int x, int y, int endX, int endY) throws InvalidPlacementException {
        // Either the start or the end must be on the field
        if (isFieldOnBoard(x, y) || isFieldOnBoard(endX, endY)) {
            return;
        }

        throw new InvalidPlacementException("at least one part of dawn must be on the board");
    }

    /**
     * Place a token at a given position
     *
     * @param token       the token type
     * @param x           the x-coord
     * @param y           the y-coord
     * @param orientation the token orientation
     * @throws InvalidPlacementException if the placement is incorrect
     */
    public void placeToken(Token token, int x, int y, Token.Orientation orientation) throws InvalidPlacementException {
        int endX = x;
        int endY = y;

        if (orientation == Token.Orientation.HORIZONTAL)
            endX = x + token.getSize() - 1;

        if (orientation == Token.Orientation.VERTICAL)
            endY = y + token.getSize() - 1;

        // Check if the coordinates are out of bounds, but for the DAWN token, special rules apply
        if (token.getSize() == Token.DAWN_SIZE) {
            throwErrorForInvalidDawnPlacement(x, y, endX, endY);
        } else {
            throwErrorForInvalidPlacement(x, y, endX, endY);
        }


        if (checkFieldsUnoccupied(x, y, endX, endY)) {
            setLineOfFieldsToChar(token.toString().charAt(0), x, y, endX, endY);
        } else {
            throw new InvalidPlacementException("some of the fields this token would inhabit are already occupied");
        }
    }

    /**
     * Check whether a field is already occupied
     * @param x the x-coordinate of the field to lookup
     * @param y the y-coordinate of the field to lookup
     * @return true if the specified field does not have a token on it
     */
    public boolean checkFieldUnoccupied(int x, int y) {
        return board[y][x] == UNOCCUPIED_FIELD;
    }

    /**
     * Checks whether the given field is within the board bounds
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the (x,y) field is inside the boards bounds
     */
    public boolean isFieldOnBoard(int x, int y) {
        return (x >= 0 && x < BOARD_WIDTH)
                && (y >= 0 && y < BOARD_HEIGHT);
    }

    private boolean checkFieldsUnoccupied(int startX, int startY, int endX, int endY) {
        assert (startX <= endX);
        assert (startY <= endY);

        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (isFieldOnBoard(x, y) && !checkFieldUnoccupied(x, y))
                    return false;
            }
        }

        return true;
    }


    /**
     * Set all the fields between the given coordinates that reside on the board to a specific character
     * @param c the character to set the fields to
     * @param startX self-explanatory
     * @param startY self-explanatory
     * @param endX self-explanatory
     * @param endY self-explanatory
     */
    private void setLineOfFieldsToChar(char c, int startX, int startY, int endX, int endY) {
        for (int x = startX; x <= endX; x++) {
            for (int y = startY; y <= endY; y++) {
                if (isFieldOnBoard(x, y))
                    board[y][x] = c;
            }
        }
    }

    /**
     * Move a token from one position to another
     * @param x1 the starting x coordinate
     * @param y1 the starting y coordinate
     * @param x2 the ending x coordinate
     * @param y2 the ending y coordinate
     */
    public void moveToken(int x1, int y1, int x2, int y2) {
        throwErrorForInvalidCoords(x1, y1);
        throwErrorForInvalidCoords(x2, y2);

        board[y2][x2] = board[y1][x1];
        board[y1][x1] = UNOCCUPIED_FIELD;
    }
    /**
     * Get string-representation of board
     * @return a 11x15 string with each character representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                sb.append(getTokenAt(x, y));
            }
            sb.append("\n");
        }

        // trim needs to be called to remove the unnecessary newline at the end
        return sb.toString().trim();
    }
}
