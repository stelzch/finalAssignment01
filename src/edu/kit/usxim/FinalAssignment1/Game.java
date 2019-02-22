package edu.kit.usxim.FinalAssignment1;

import edu.kit.usxim.FinalAssignment1.exceptions.*;

import java.util.List;

/**
 * This class brings together all the other components
 */
public class Game implements PlayerCommandExecutor {
    private static final int DICE_NOT_YET_ROLLED = -1;
    private static final int NUMBER_OF_ROUNDS_PER_PHASE = 6;
    public enum GameState {
        /** The initial state: VC has to be placed */
        VC_PLACEMENT_EXPECTED,
        /** The next move has to be a dice roll */
        DICE_ROLL_EXPECTED,
        /** The next move has to be a token placement */
        TOKEN_PLACEMENT_EXPECTED,
        /** The next move has to be the placement of vesta or ceres */
        VC_MOVEMENT_EXPECTED,
        /** The current game is finished, no moves can be made */
        GAME_FINISHED
    }

    public enum GamePhase {
        /** The game is in the first (vesta) phase */
        PHASE_ONE,
        /** The game is in the second (ceres) phase */
        PHASE_TWO
    }

    private MissionControlTokenSet tokensForPhaseOne;
    private MissionControlTokenSet tokensForPhaseTwo;
    private NatureTokenSet natureTokenSet;
    private Board board;


    private GameState state;
    private GamePhase phase;

    private int lastDiceRoll;
    private int currentRound;

    /**
     * Default constructor
     */
    public Game() {
        tokensForPhaseOne = new MissionControlTokenSet();
        tokensForPhaseTwo = new MissionControlTokenSet();
        board = new Board();
        natureTokenSet = new NatureTokenSet(board);

        // At the start, we are in phase one and the VC must be placed
        phase = GamePhase.PHASE_ONE;
        state = GameState.VC_PLACEMENT_EXPECTED;

        // Make it invalid to signal no dice has been rolled yet
        lastDiceRoll = DICE_NOT_YET_ROLLED;
        currentRound = 0;
    }

    /**
     * If the current state does not match the expectedStateForCommand, it throws an exception
     * @param expectedStateForCommand the state the game should be in in order for the command to be run properly
     */
    private void throwErrorIfRequestStateMismatch(GameState expectedStateForCommand) throws InvalidCommandException {
        if (expectedStateForCommand != state) {
            throw new InvalidCommandException("this command was not expected");
        }
    }

    /**
     * Throw an exception if the dice number has not been set before
     */
    private void throwErrorIfDiceNumberUnset() {
        if (lastDiceRoll == DICE_NOT_YET_ROLLED)
            throw new IllegalStateException("the dice should be rolled beforehand");
    }


    /**
     * @return the state the game is currently in
     */
    public GameState getState() {
        return state;
    }

    private boolean shouldMoveToNextRound() {
        return state == GameState.VC_PLACEMENT_EXPECTED
                && currentRound == (NUMBER_OF_ROUNDS_PER_PHASE - 1);
    }

    private GameState getNextState() {
        int currentStateIndex = state.ordinal();
        int nextStateIndex = (currentStateIndex + 1) % GameState.values().length;

        return GameState.values()[nextStateIndex];
    }

    private boolean gameIsFinished() {
        if (phase == GamePhase.PHASE_TWO
                && currentRound == (NUMBER_OF_ROUNDS_PER_PHASE - 1)) {
            return true;
        } else {
            return false;
        }
    }

    private void moveToNextPhase() {
        state = GameState.VC_PLACEMENT_EXPECTED;
        currentRound = 0;
        phase = GamePhase.PHASE_TWO;
    }

    private void startNextRound() {
        currentRound += 1;
        state = GameState.DICE_ROLL_EXPECTED;
    }

    private boolean atEndOfPhase() {
        return currentRound == (NUMBER_OF_ROUNDS_PER_PHASE - 1);
    }

    /**
     * Move the game to the next state
     */
    public void moveToNextState() {
        GameState nextState = getNextState();

        // If the game is finished, we do not progress through states anymore
        if (gameIsFinished() && nextState == GameState.GAME_FINISHED) {
            state = GameState.GAME_FINISHED;
            return;
        }

        if (nextState == GameState.GAME_FINISHED) {
            // A round has been completed

            if (atEndOfPhase()) {
                // This was the last round of the current phase
                moveToNextPhase();
            } else {
                // Just start the next round
                startNextRound();
            }
        } else {

            if (nextState == GameState.VC_MOVEMENT_EXPECTED && vcCanStillMove() == false) {
                // Skip it
                if (gameIsFinished() || atEndOfPhase()) {
                    moveToNextPhase();
                } else {
                    state = GameState.DICE_ROLL_EXPECTED;
                }
                return;
            }

            state = nextState;
        }

    }

    private boolean vcCanStillMove() {
        try {
            return natureTokenSet.getNumOfReachableFields(phase) > 0;
        } catch (InvalidCoordinatesException e) {
            return true;
        }
    }

    /**
     * Get the dice number from a string representation and ensure its in the right bounds
     * @param symbol a string representing the dice number (2-6 or DAWN)
     * @return an integer between 2 and 7
     */
    private int parseDiceSymbol(String symbol) throws InvalidDiceNumberException {
        if (symbol.matches("[2-6]")) {
            return Integer.parseInt(symbol);
        } else if (symbol.equals("DAWN")) {
            return 7;
        } else {
            throw new InvalidDiceNumberException("the dice symbol is invalid");
        }
    }

    private PlayingTokenSet getTokenSetForPhase() {
        switch (phase) {
            case PHASE_ONE:
                return tokensForPhaseOne;
            case PHASE_TWO:
                return tokensForPhaseTwo;
            default:
                throw new IllegalArgumentException("this game phase does not exist");
        }
    }

    /**
     * Get the token for the currently rolled dice number
     * @param anticipatedTokenSize the size the token should be
     * @return a token of the anticipated token size
     * @throws IllegalArgumentException if the anticipated token size does not match with the last dice roll
     * @throws IllegalArgumentException if there is no token left that matches the anticipated size
     */
    private Token getTokenForDiceNumber(int anticipatedTokenSize) throws InvalidPlacementException {
        throwErrorIfDiceNumberUnset();
        List<Token> availableTokens;
        try {
            availableTokens = getTokenSetForPhase().getPossibleTokensForDiceNumber(lastDiceRoll);

            StringBuilder sb = new StringBuilder();
            for (Token possibleToken : availableTokens) {
                sb.append(possibleToken.getSize());
                sb.append(" ");
                if (possibleToken.getSize() == anticipatedTokenSize) {
                    return possibleToken;
                }
            }

            throw new InvalidPlacementException("no token of that size left, currently available: " + sb.toString());
        } catch (InvalidDiceNumberException e) {
            throw new IllegalStateException("the last dice roll was invalid");
        }
    }
    @Override
    public String state(Coordinates pos) throws InvalidCoordinatesException {
        char field = board.getTokenAt(pos);
        return String.valueOf(field);
    }

    @Override
    public String print() {
        return board.toString();
    }

    @Override
    public String setVC(Coordinates pos) throws GameException {
        throwErrorIfRequestStateMismatch(GameState.VC_PLACEMENT_EXPECTED);
        natureTokenSet.placeVC(phase, pos);

        moveToNextState();

        return "OK";
    }

    @Override
    public String roll(String symbol) throws InvalidDiceNumberException, InvalidCommandException {
        throwErrorIfRequestStateMismatch(GameState.DICE_ROLL_EXPECTED);

        lastDiceRoll = parseDiceSymbol(symbol);

        moveToNextState();

        return "OK";
    }

    @Override
    public String place(Coordinates start, Coordinates end)
            throws InvalidPlacementException, InvalidCoordinatesException, InvalidCommandException {
        throwErrorIfRequestStateMismatch(GameState.TOKEN_PLACEMENT_EXPECTED);
        throwErrorIfDiceNumberUnset();

        Token.Orientation or = Utils.getLineOrientation(start, end);
        int requiredTokenSize = Utils.getStraightLineLength(start, end);

        Token t = getTokenForDiceNumber(requiredTokenSize);

        int x = Math.min(start.getX(), end.getX());
        int y = Math.min(start.getY(), end.getY());

        board.placeToken(t, new Coordinates(x, y), or);
        getTokenSetForPhase().removeToken(t);

        moveToNextState();

        return "OK";
    }

    @Override
    public String move(List<ElementaryTokenMove> moves)
            throws InvalidMoveException, InvalidCoordinatesException, InvalidCommandException {
        throwErrorIfRequestStateMismatch(GameState.VC_MOVEMENT_EXPECTED);
        throwErrorIfDiceNumberUnset();


        int stepsRequiredForMove = natureTokenSet.countStepsAndCheckIfLegal(phase, moves);
        if (stepsRequiredForMove > lastDiceRoll)
            throw new InvalidMoveException("this step requires a dice roll of " + stepsRequiredForMove);

        if (stepsRequiredForMove == 0)
            throw new InvalidMoveException("need to specify at least move");

        natureTokenSet.moveVC(phase, moves);

        moveToNextState();

        return "OK";
    }

    @Override
    public int result() throws GameException {
        throwErrorIfRequestStateMismatch(GameState.GAME_FINISHED);

        return natureTokenSet.getScore();
    }

}
