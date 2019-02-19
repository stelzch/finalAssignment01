package edu.kit.usxim.FinalAssignment1;

public class FreeFieldCounter {
    private static final char TOKEN_VISITED = '*';
    private Board board;
    private int fieldsVisited = 0;
    private int targetX;
    private int targetY;

    /**
     *
     * @param b
     * @param x
     * @param y
     */
    public FreeFieldCounter(Board b, int x, int y) {
        board = new Board(b);

        targetX = x;
        targetY = y;

        board.setTokenAt(x, y, TOKEN_VISITED);
        floodFillNeighbours(x, y);
    }

    private void floodFillNeighbours(int x, int y) {
        if (x > 0)
            floodFill(x - 1, y); // left
        if (x < Board.BOARD_WIDTH - 1)
            floodFill(x + 1, y); // right
        if (y > 0)
            floodFill(x, y - 1); // below
        if (y < Board.BOARD_HEIGHT - 1)
            floodFill(x, y + 1); // above
    }

    private void floodFill(int x, int y) {
        if (board.getTokenAt(x, y) == Board.UNOCCUPIED_FIELD) {
            fieldsVisited += 1;
            board.setTokenAt(x, y, TOKEN_VISITED);

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
