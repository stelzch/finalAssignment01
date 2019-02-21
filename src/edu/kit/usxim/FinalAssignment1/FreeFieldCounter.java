package edu.kit.usxim.FinalAssignment1;

import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;

public class FreeFieldCounter {
    private static final char TOKEN_VISITED = '*';
    private Board board;
    private int fieldsVisited = 0;
    private Coordinates targetPos;

    /**
     * @param b the board of which to count the free fields
     * @param pos the target coordinates
     * @throws InvalidCoordinatesException if the target coordinates provided were invalid
     */
    public FreeFieldCounter(Board b, Coordinates pos) throws InvalidCoordinatesException {
        board = new Board(b);

        targetPos = pos;

        board.setTokenAt(targetPos, TOKEN_VISITED);
        floodFillNeighbours(targetPos.getX(), targetPos.getY());
    }

    private void floodFillNeighbours(int x, int y) throws InvalidCoordinatesException {
        if (x > 0)
            floodFill(x - 1, y); // left
        if (x < Board.BOARD_WIDTH - 1)
            floodFill(x + 1, y); // right
        if (y > 0)
            floodFill(x, y - 1); // below
        if (y < Board.BOARD_HEIGHT - 1)
            floodFill(x, y + 1); // above
    }

    private void floodFill(int x, int y) throws InvalidCoordinatesException {
        if (board.getTokenAt(new Coordinates(x, y)) == Board.UNOCCUPIED_FIELD) {
            fieldsVisited += 1;
            board.setTokenAt(new Coordinates(x, y), TOKEN_VISITED);

            floodFillNeighbours(x, y);
        }
    }
    /**
     * @return the number of reachable free fields given by the target coordinates
     */
    public int countReachableTokens() {
        return fieldsVisited;
    }
}
