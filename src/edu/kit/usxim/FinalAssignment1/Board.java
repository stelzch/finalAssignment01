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

    private boolean checkFieldUnoccupied(int x, int y) {
        return board[y][x] == UNOCCUPIED_FIELD;
    }

    /**
     * Checks whether the given field is within the board bounds
     * @param x the x-coordinate
     * @param y the y-coordinate
     * @return true if the (x,y) field is inside the boards bounds
     */
    private boolean isFieldOnBoard(int x, int y) {
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
     * Move a token to a given position
     * @param srcX the source x-coord
     * @param srcY the source y-coord
     * @param move the move to execute
     * @throws InvalidMoveException if the coordinates are out of bounds or the target field is already occupied
     */
    public void executeTokenMove(int srcX, int srcY, ElementaryTokenMove move) throws InvalidMoveException {
        int dstX = move.getDstX();
        int dstY = move.getDstY();
        throwErrorForInvalidCoords(srcX, srcY);
        throwErrorForInvalidCoords(dstX, dstY);

        if (!checkFieldUnoccupied(dstX, dstY))
            throw new InvalidMoveException(move);

        board[dstY][dstX] = board[srcY][srcX];
        board[srcY][srcX] = UNOCCUPIED_FIELD;
    }

    /**
     * Execute the collection of moves on the token at the given position
     * @param x the x-coordinate of the token to moves apply to
     * @param y the y-coordinate of the token to moves apply to
     * @param moves a collection of cohesive moves
     * @throws InvalidMoveException if any of the moves does not comply with the elementary rules
     */
    public void executeMoves(int x, int y, Collection<ElementaryTokenMove> moves) throws InvalidMoveException {
        if (checkFieldUnoccupied(x, y))
            throw new IllegalArgumentException("the starting coordinates were incorrect");

        int fromX = x;
        int fromY = y;
        for (ElementaryTokenMove move : moves) {
            if (!checkFieldUnoccupied(move.getDstX(), move.getDstY()))
                throw new InvalidMoveException(move);

            executeTokenMove(fromX, fromY, move);

            fromX = move.getDstX();
            fromY = move.getDstY();
        }
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
