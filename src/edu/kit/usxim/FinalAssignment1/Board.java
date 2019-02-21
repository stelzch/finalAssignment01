package edu.kit.usxim.FinalAssignment1;

import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidPlacementException;

/**
 * The board stores the current game state
 */
public class Board {
    /** How wide the board is */
    public static final int BOARD_WIDTH = 15;
    /** How high the board is */
    public static final int BOARD_HEIGHT = 11;

    /** The character to represent an unoccupied field */
    public static final char UNOCCUPIED_FIELD = '-';

    private char[][] board;

    /**
     * Default constructor
     */
    public Board() {
        board = new char[BOARD_HEIGHT][BOARD_WIDTH];

        setFieldsToDefault(UNOCCUPIED_FIELD);
    }

    /**
     * Copy constructor
     * @param other another board to copy
     */
    public Board(Board other) {
        board = new char[BOARD_HEIGHT][BOARD_WIDTH];

        Coordinates lookupCoordinates = new Coordinates(0, 0);
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                lookupCoordinates.setX(x);
                lookupCoordinates.setY(y);
                try {
                    board[y][x] = other.getTokenAt(lookupCoordinates);
                } catch (InvalidCoordinatesException e){
                    throw new RuntimeException("Board dimension coordinates are invalid");
                }
            }
        }

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
     * @param target the target coordinates
     * @return the character representation at the given coordinates
     */
    public char getTokenAt(Coordinates target) throws InvalidCoordinatesException {
        throwErrorForInvalidCoords(target);
        return board[target.getY()][target.getX()];
    }

    /**
     * Set the field state
     * @param target the target coordinates
     * @param c the character to set it to
     */
    public void setTokenAt(Coordinates target, char c) throws InvalidCoordinatesException {
        throwErrorForInvalidCoords(target);
        board[target.getY()][target.getX()] = c;
    }


    private void throwErrorForInvalidCoords(Coordinates coords) throws InvalidCoordinatesException {
        int x = coords.getX();
        int y = coords.getY();

        if (x < 0 || y < 0)
            throw new InvalidCoordinatesException("coordinates must be positive integers!");

        if (x > (BOARD_WIDTH - 1))
            throw new InvalidCoordinatesException("x coordinate must be smaller than " + BOARD_WIDTH);

        if (y > (BOARD_HEIGHT - 1))
            throw new InvalidCoordinatesException("y coordinate must be bigger than " + BOARD_HEIGHT);
    }

    private void throwErrorForInvalidPlacement(Coordinates start, Coordinates end) throws InvalidPlacementException, InvalidCoordinatesException {
        throwErrorForInvalidCoords(start);
        throwErrorForInvalidCoords(end);

        if ((start.getX() > end.getX()) || (start.getY() > end.getY())) {
            throw new InvalidPlacementException("the starting coordinates must be smaller");
        }
    }

    private void throwErrorForInvalidDawnPlacement(Coordinates start, Coordinates end) throws InvalidPlacementException {
        // Either the start or the end must be on the field
        if (isFieldOnBoard(start) || isFieldOnBoard(end)) {
            return;
        }

        throw new InvalidPlacementException("at least one part of dawn must be on the board");
    }

    /**
     * Place a token at a given position
     *
     * @param token       the token type
     * @param pos the position where to place the coordinates
     * @param orientation the token orientation
     * @throws InvalidPlacementException if the placement is incorrect
     */
    public void placeToken(Token token, Coordinates pos, Token.Orientation orientation) throws InvalidPlacementException, InvalidCoordinatesException {
        int endX = pos.getX();
        int endY = pos.getY();

        if (orientation == Token.Orientation.HORIZONTAL)
            endX = pos.getX() + token.getSize() - 1;

        if (orientation == Token.Orientation.VERTICAL)
            endY = pos.getY() + token.getSize() - 1;


        Coordinates endingCoordinates = new Coordinates(endX, endY);
        // Check if the coordinates are out of bounds, but for the DAWN token, special rules apply
        if (token.getSize() == Token.DAWN_SIZE) {
            throwErrorForInvalidDawnPlacement(pos, endingCoordinates);
        } else {
            throwErrorForInvalidPlacement(pos, endingCoordinates);
        }


        if (checkFieldsUnoccupied(pos, endingCoordinates)) {
            setLineOfFieldsToChar(token.toString().charAt(0),
                    pos.getX(), pos.getY(), endingCoordinates.getX(), endingCoordinates.getY());
        } else {
            throw new InvalidPlacementException("some of the fields this token would inhabit are already occupied");
        }
    }

    /**
     * Check whether a field is already occupied
     * @param pos the position of the field
     * @return true if the specified field does not have a token on it
     */
    public boolean checkFieldUnoccupied(Coordinates pos) throws InvalidCoordinatesException {
        return getTokenAt(pos) == UNOCCUPIED_FIELD;
    }

    /**
     * Checks whether the given field is within the board bounds
     * @param fieldCoord the position of the field
     * @return true if the (x,y) field is inside the boards bounds
     */
    public boolean isFieldOnBoard(Coordinates fieldCoord) {
        return (fieldCoord.getX() >= 0 && fieldCoord.getX() < BOARD_WIDTH)
                && (fieldCoord.getY() >= 0 && fieldCoord.getY() < BOARD_HEIGHT);
    }

    private boolean checkFieldsUnoccupied(Coordinates start, Coordinates end) throws InvalidCoordinatesException {
        assert (start.getX() <= end.getX());
        assert (start.getY() <= end.getY());

        for (int x = start.getX(); x <= end.getX(); x++) {
            for (int y = start.getY(); y <= end.getY(); y++) {
                Coordinates currentCoordinates = new Coordinates(x, y);
                if (isFieldOnBoard(currentCoordinates)
                        && !checkFieldUnoccupied(currentCoordinates))
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
                if (isFieldOnBoard(new Coordinates(x, y)))
                    board[y][x] = c;
            }
        }
    }

    /**
     * Move a token from one position to another
     * @param start the starting coordinates
     * @param end the ending coordinates
     */
    public void moveToken(Coordinates start, Coordinates end) throws InvalidCoordinatesException {
        throwErrorForInvalidCoords(start);
        throwErrorForInvalidCoords(end);

        board[end.getY()][end.getX()] = board[start.getY()][start.getX()];
        board[start.getY()][start.getX()] = UNOCCUPIED_FIELD;
    }
    /**
     * Get string-representation of board
     * @return a 11x15 string with each character representation
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Coordinates lookupCoords = new Coordinates(0, 0);
        for (int y = 0; y < BOARD_HEIGHT; y++) {
            for (int x = 0; x < BOARD_WIDTH; x++) {
                lookupCoords.setX(x);
                lookupCoords.setY(y);
                try {
                    sb.append(getTokenAt(lookupCoords));
                } catch (InvalidCoordinatesException e) {
                    throw new RuntimeException("loop boundaries defined wrong");
                }
            }
            sb.append("\n");
        }

        // trim needs to be called to remove the unnecessary newline at the end
        return sb.toString().trim();
    }
}
