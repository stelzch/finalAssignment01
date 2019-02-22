package edu.kit.usxim.FinalAssignment1;


import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidMoveException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidPlacementException;

import java.util.List;

public class NatureTokenSet {
    private Token vesta;
    private Token ceres;

    /** The coordinates of the placed tokens are needed to move them */
    private Coordinates ceresPosition;
    private Coordinates vestaPosition;

    /** The Board the game is placed on */
    private Board board;


    /**
     * Default constructor
     * @param board the board the Vesta and Ceres tokens will later be placed on
     */
    public NatureTokenSet(Board board) {
        vesta = new Token(Token.Type.VESTA, Token.VESTA_OR_CERES_SIZE);
        ceres = new Token(Token.Type.CERES, Token.VESTA_OR_CERES_SIZE);

        vestaPosition = new Coordinates(-1, -1);
        ceresPosition = new Coordinates(-1, -1);

        this.board = board;
    }

    /**
     * Checks whether the relevant token for the given game phase has already been placed on the board
     * @param phase the phase the game is currently in (determines whether Vesta or Ceres is used)
     * @return true if the relevant token is already on the board, false if it is not
     */
    private boolean hasTokenAlreadyBeenPlaced(Game.GamePhase phase) {
        if (phase == Game.GamePhase.PHASE_ONE)  {
            return (vestaPosition.getX() != -1 && vestaPosition.getY() != -1);
        } else {
            return (ceresPosition.getX() != -1 && ceresPosition.getY() != -1);
        }
    }

    private String getNameOfRelevantTokenForPhase(Game.GamePhase phase) {
        return getTokenRelevantForPhase(phase).getType().toString().toLowerCase();
    }

    private Token getTokenRelevantForPhase(Game.GamePhase phase) {
        return (phase == Game.GamePhase.PHASE_ONE) ? vesta : ceres;
    }

    private Coordinates getCoordinatesRelevantForPhase(Game.GamePhase phase) {
        return (phase == Game.GamePhase.PHASE_ONE) ? vestaPosition : ceresPosition;
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

    private void updateCoordinatesToNewPosition(Game.GamePhase phase, Coordinates newPos) {
        getCoordinatesRelevantForPhase(phase).setX(newPos.getX());
        getCoordinatesRelevantForPhase(phase).setY(newPos.getY());
    }


    /**
     * Place the Vesta/Ceres token at the given coordinates
     * @param phase the phase the game is currently in
     * @param pos the position to set V/C to
     * @throws InvalidPlacementException if the coordinates are incorrect or the target coordinates already occupied
     * @throws InvalidCoordinatesException if the target coordinates were incorrect
     */
    public void placeVC(Game.GamePhase phase, Coordinates pos)
            throws InvalidPlacementException, InvalidCoordinatesException {
        throwErrorIfAlreadyPlaced(phase);
        Token relevantToken = getTokenRelevantForPhase(phase);
        board.placeToken(relevantToken, pos, Token.Orientation.VERTICAL);

        updateCoordinatesToNewPosition(phase, pos);
    }

    private void throwErrorIfMovesNotConnected(ElementaryTokenMove from, ElementaryTokenMove to)
            throws InvalidMoveException {
        if (from.isConnectedTo(to) == false) {
            StringBuilder sb = new StringBuilder("impossible to move to ");
            sb.append(to);

            throw new InvalidMoveException(sb.toString());
        }
    }

    private boolean fieldIsVC(Coordinates field, Game.GamePhase phase) throws InvalidCoordinatesException {
        char vcInitial = getTokenRelevantForPhase(phase).toString().charAt(0);

        return board.getTokenAt(field)  == vcInitial;
    }

    private void throwErrorIfDestinationOccupied(ElementaryTokenMove move, Game.GamePhase phase)
            throws InvalidMoveException, InvalidCoordinatesException {
        if (board.checkFieldUnoccupied(move) == false
                && !fieldIsVC(move, phase)) {
            StringBuilder sb = new StringBuilder("cant move through ");
            sb.append(move);
            sb.append(" - field is occupied");

            throw new InvalidMoveException(sb.toString());
        }
    }

    private void throwErrorIfCoordinatesInvalid(ElementaryTokenMove move) throws InvalidMoveException {
        if (!(board.isFieldOnBoard(move))) {
            StringBuilder sb = new StringBuilder("invalid coordinates - ");
            sb.append(move);

            throw new InvalidMoveException(sb.toString());
        }
    }

    private ElementaryTokenMove getInitialMoveForPhase(Game.GamePhase phase) {
        ElementaryTokenMove move = new ElementaryTokenMove(getCoordinatesRelevantForPhase(phase));

        return move;
    }

    /**
     * Follows the given path of elementary moves and checks if they comply with the game rules
     * @param phase the phase the game is currently in
     * @param moves a list of moves to execute
     * @return the number of steps taken (without doubles)
     * @throws InvalidMoveException if any of the moves is illegal
     * @throws InvalidCoordinatesException if any of the coordinates specified within the moves are faulty
     */
    public int countStepsAndCheckIfLegal(Game.GamePhase phase, List<ElementaryTokenMove> moves)
            throws InvalidMoveException, InvalidCoordinatesException {
        int stepsNeeded = 0;

        ElementaryTokenMove lastMove = getInitialMoveForPhase(phase);

        for (ElementaryTokenMove nextMove : moves) {
            throwErrorIfMovesNotConnected(lastMove, nextMove);
            throwErrorIfDestinationOccupied(nextMove, phase);
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
     * @throws InvalidCoordinatesException if any of the coordinates specified within the moves are faulty
     */
    public void moveVC(Game.GamePhase phase, List<ElementaryTokenMove> moves)
            throws InvalidMoveException, InvalidCoordinatesException {
        throwErrorIfNotYetPlaced(phase);
        countStepsAndCheckIfLegal(phase, moves);

        // By this point, all exceptions should be eliminated, so we can skip right ahead
        ElementaryTokenMove initialMove = getInitialMoveForPhase(phase);
        ElementaryTokenMove lastMove = moves.get(moves.size() - 1);
        board.moveToken(initialMove, lastMove);
        updateCoordinatesToNewPosition(phase, lastMove);
    }

    /**
     * Calculate the number of fields that are reachable from the position of Vesta/Ceres
     * @param phase the game phase, to determine whether to use Vesta or Ceres position
     * @return the number of fields reachable from V/C's location, excluding the field its standing on
     * @throws IllegalStateException if V/C has not been placed yet
     * @throws InvalidCoordinatesException when an internal error happens
     */
    public int getNumOfReachableFields(Game.GamePhase phase) throws InvalidCoordinatesException, IllegalStateException {
        try {
            throwErrorIfNotYetPlaced(phase);
        } catch (InvalidMoveException e) {
            throw new IllegalStateException(e.getMessage());
        }

        FreeFieldCounter counter = new FreeFieldCounter(board, getCoordinatesRelevantForPhase(phase));

        return counter.countReachableTokens();
    }
}
