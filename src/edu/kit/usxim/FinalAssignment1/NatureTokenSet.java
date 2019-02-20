package edu.kit.usxim.FinalAssignment1;


import java.util.List;

public class NatureTokenSet {
    private Token vesta;
    private Token ceres;

    /** The coordinates of the placed tokens are needed to move them */
    private int ceresX;
    private int ceresY;
    private int vestaX;
    private int vestaY;

    /** The Board the game is placed on */
    private Board board;


    /**
     * Default constructor
     * @param board the board the Vesta and Ceres tokens will later be placed on
     */
    public NatureTokenSet(Board board) {
        vesta = new Token(Token.Type.VESTA, Token.VESTA_OR_CERES_SIZE);
        ceres = new Token(Token.Type.CERES, Token.VESTA_OR_CERES_SIZE);

        ceresX = -1;
        ceresY = -1;
        vestaX = -1;
        vestaY = -1;

        this.board = board;
    }

    /**
     * Checks whether the relevant token for the given game phase has already been placed on the board
     * @param phase the phase the game is currently in (determines whether Vesta or Ceres is used)
     * @return true if the relevant token is already on the board, false if it is not
     */
    private boolean hasTokenAlreadyBeenPlaced(Game.GamePhase phase) {
        if (phase == Game.GamePhase.PHASE_ONE)  {
            return (vestaX != -1 && vestaY != -1);
        } else {
            return (ceresX != -1 && ceresY != -1);
        }
    }

    private String getNameOfRelevantTokenForPhase(Game.GamePhase phase) {
        return getRelevantTokenForPhase(phase).getType().toString().toLowerCase();
    }

    /**
     * If the token for the current phase has already been placed, throw an error
     * @param phase
     * @throws InvalidPlacementException
     */
    private void throwErrorIfAlreadyPlaced(Game.GamePhase phase) throws InvalidPlacementException {
        if (hasTokenAlreadyBeenPlaced(phase) == true) {
            StringBuilder sb = new StringBuilder();
            sb.append(getNameOfRelevantTokenForPhase(phase));
            sb.append(" has already been placed");

            throw new InvalidPlacementException(sb.toString());
        }
    }

    /**
     * If the token for the current phase has not been placed, throw an error
     */
    private void throwErrorIfNotYetPlaced(Game.GamePhase phase) throws InvalidMoveException {
        if (hasTokenAlreadyBeenPlaced(phase) == false) {
            StringBuilder sb = new StringBuilder();
            sb.append(getNameOfRelevantTokenForPhase(phase));
            sb.append(" has not been placed yet");

            throw new InvalidMoveException(sb.toString());
        }
    }

    private void updateCoordinatesToNewPosition(Game.GamePhase phase, int newX, int newY) {
        if (phase == Game.GamePhase.PHASE_ONE) {
            vestaX = newX;
            vestaY = newY;
        } else {
            ceresX = newX;
            ceresY = newY;
        }
    }

    private Token getRelevantTokenForPhase(Game.GamePhase phase) {
        return (phase == Game.GamePhase.PHASE_ONE) ? vesta : ceres;
    }

    /**
     * Place the Vesta/Ceres token at the given coordinates
     * @param phase the phase the game is currently in
     * @param x the x coordinate
     * @param y the y coordinate
     * @throws InvalidPlacementException if the coordinates are incorrect or the target coordinates already occupied
     */
    public void placeVC(Game.GamePhase phase, int x, int y) throws InvalidPlacementException {
        throwErrorIfAlreadyPlaced(phase);
        Token relevantToken = getRelevantTokenForPhase(phase);
        board.placeToken(relevantToken, x, y, Token.Orientation.VERTICAL);

        updateCoordinatesToNewPosition(phase, x, y);
    }

    private void throwErrorIfMovesNotConnected(ElementaryTokenMove from, ElementaryTokenMove to)
            throws InvalidMoveException {
        if (from.isConnectedTo(to) == false) {
            StringBuilder sb = new StringBuilder("impossible to move to ");
            sb.append(to);

            throw new InvalidMoveException(sb.toString());
        }
    }

    private void throwErrorIfDestinationOccupied(ElementaryTokenMove move) throws InvalidMoveException {
        if (board.checkFieldUnoccupied(move.getDstX(), move.getDstY()) == false) {
            StringBuilder sb = new StringBuilder("cant move through ");
            sb.append(move);
            sb.append(" - field is occupied");

            throw new InvalidMoveException(sb.toString());
        }
    }

    private void throwErrorIfCoordinatesInvalid(ElementaryTokenMove move) throws InvalidMoveException {
        if (!(board.isFieldOnBoard(move.getDstX(), move.getDstY()))) {
            StringBuilder sb = new StringBuilder("invalid coordinates - ");
            sb.append(move);

            throw new InvalidMoveException(sb.toString());
        }
    }

    private ElementaryTokenMove getInitialMoveForPhase(Game.GamePhase phase) {
        int posX = (getRelevantTokenForPhase(phase) == vesta) ? vestaX : ceresX;
        int posY = (getRelevantTokenForPhase(phase) == vesta) ? vestaY : ceresY;
        ElementaryTokenMove move = new ElementaryTokenMove(posX, posY);

        return move;
    }

    /**
     * Follows the given path of elementary moves and checks if they comply with the game rules
     * @param phase the phase the game is currently in
     * @param moves a list of moves to execute
     * @return the number of steps taken (without doubles)
     * @throws InvalidMoveException if any of the moves is illegal
     */
    public int countStepsAndCheckIfLegal(Game.GamePhase phase, List<ElementaryTokenMove> moves)
            throws InvalidMoveException {
        int stepsNeeded = 0;

        ElementaryTokenMove lastMove = getInitialMoveForPhase(phase);

        for (ElementaryTokenMove nextMove : moves) {
            throwErrorIfMovesNotConnected(lastMove, nextMove);
            throwErrorIfDestinationOccupied(nextMove);
            throwErrorIfCoordinatesInvalid(nextMove);

            if (!nextMove.equals(lastMove))
                stepsNeeded++;

            lastMove = nextMove;
        }

        return stepsNeeded;

    }

    /**
     * Move Vesta or Ceres accordingly to the provided elementary steps
     * @param phase the phase the game is currently in (determines whether to move vesta or ceres)
     * @param moves a list of elementary moves to move along
     * @throws InvalidMoveException if one of the moves was illegal
     */
    public void moveVC(Game.GamePhase phase, List<ElementaryTokenMove> moves) throws InvalidMoveException {
        throwErrorIfNotYetPlaced(phase);
        countStepsAndCheckIfLegal(phase, moves);

        // By this point, all exceptions should be eliminated, so we can skip right ahead
        ElementaryTokenMove initialMove = getInitialMoveForPhase(phase);
        ElementaryTokenMove lastMove = moves.get(moves.size() - 1);
        board.moveToken(initialMove.getDstX(), initialMove.getDstY(), lastMove.getDstX(), lastMove.getDstY());
        updateCoordinatesToNewPosition(phase, lastMove.getDstX(), lastMove.getDstY());
    }

    /**
     * Calculate the number of fields that are reachable from the position of Vesta/Ceres
     * @param phase the game phase, to determine whether to use Vesta or Ceres position
     * @return the number of fields reachable from V/C's location, excluding the field its standing on
     * @throws IllegalStateException if V/C has not been placed yet
     */
    public int getNumOfReachableFields(Game.GamePhase phase) {
        try {
            throwErrorIfNotYetPlaced(phase);
        } catch (InvalidMoveException e) {
            throw new IllegalStateException(e.getMessage());
        }
        int targetX = (phase == Game.GamePhase.PHASE_ONE) ? vestaX : ceresX;
        int targetY = (phase == Game.GamePhase.PHASE_ONE) ? vestaY : ceresY;

        FreeFieldCounter counter = new FreeFieldCounter(board, targetX, targetY);

        return counter.countReachableTokens();
    }
}
