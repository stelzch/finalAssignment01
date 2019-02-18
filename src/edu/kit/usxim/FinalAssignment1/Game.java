package edu.kit.usxim.FinalAssignment1;

import java.util.List;

/**
 * This class brings together all the other components
 */
public class Game implements PlayerCommandExecutor {
    private static final int DICE_NOT_YET_ROLLED = -1;
    public enum GameState {
        /** The initial state: VC has to be placed */
        VC_PLACEMENT_EXPECTED,
        /** The next move has to be a dice roll */
        DICE_ROLL_EXPECTED,
        /** The next move has to be a token placement */
        TOKEN_PLACEMENT_EXPECTED,
        /** The next move has to be the placement of vesta or ceres */
        VC_MOVEMENT_EXPECTED
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
    }

    /**
     * If the current state does not match the expectedStateForCommand, it throws an exception
     * @param expectedStateForCommand the state the game should be in in order for the command to be run properly
     */
    private void throwErrorIfRequestStateMismatch(GameState expectedStateForCommand) {
        if (expectedStateForCommand != state) {
            throw new IllegalStateException("this command was not expected");
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
    /**
     * Move the game to the next state
     */
    public void moveToNextState() {
        int currentStateIndex = state.ordinal();
        int nextStateIndex = (currentStateIndex + 1) % GameState.values().length;

        state = GameState.values()[nextStateIndex];

        // Skip the VC_PLACEMENT state, as it only is expected initially
        if (state == GameState.VC_PLACEMENT_EXPECTED)
            state = GameState.DICE_ROLL_EXPECTED;
    }

    /**
     * Get the dice number from a string representation and ensure its in the right bounds
     * @param symbol a string representing the dice number (2-6 or DAWN)
     * @return an integer between 2 and 7
     */
    private int parseDiceSymbol(String symbol) {
        if (symbol.matches("[2-7]")) {
            return Integer.parseInt(symbol);
        } else if (symbol.equals("DAWN")) {
            return 7;
        } else {
            throw new IllegalArgumentException("the dice symbol is invalid");
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
    private Token getTokenForDiceNumber(int anticipatedTokenSize) {
        throwErrorIfDiceNumberUnset();
        if (anticipatedTokenSize != lastDiceRoll)
            throw new IllegalArgumentException("the requested token size does not equal to the last roll");

        List<Token> availableTokens;
        try {
            availableTokens = getTokenSetForPhase().getPossibleTokensForDiceNumber(lastDiceRoll);
        } catch (InvalidDiceNumberException e) {
            throw new IllegalStateException("the last dice roll was invalid");
        }

        for (Token t : availableTokens) {
            if (t.getSize() == anticipatedTokenSize)
                return t;
        }

        throw new IllegalArgumentException("there is no token left with the requested size");
    }

    @Override
    public String state(int m, int n) {
        char field = board.getTokenAt(n, m);
        return String.valueOf(field);
    }

    @Override
    public String print() {
        return board.toString();
    }

    @Override
    public String setVC(int m, int n) throws InvalidPlacementException, IllegalAccessException {
        throwErrorIfRequestStateMismatch(GameState.VC_PLACEMENT_EXPECTED);
        natureTokenSet.placeVC(phase, n, m);

        moveToNextState();

        return "OK";
    }

    @Override
    public String roll(String symbol) {
        throwErrorIfRequestStateMismatch(GameState.DICE_ROLL_EXPECTED);

        lastDiceRoll = parseDiceSymbol(symbol);

        moveToNextState();

        return "OK";
    }

    @Override
    public String place(int x1, int y1, int x2, int y2) throws InvalidPlacementException {
        throwErrorIfRequestStateMismatch(GameState.TOKEN_PLACEMENT_EXPECTED);
        throwErrorIfDiceNumberUnset();

        Token.Orientation or = Utils.getLineOrientation(x1, y1, x2, y2);
        int requiredTokenSize = Utils.getStraightLineLength(x1, y1, x2, y2);

        Token t = getTokenForDiceNumber(requiredTokenSize);

        int x = Math.min(x1, x2);
        int y = Math.min(y1, y2);

        board.placeToken(t, x, y, or);

        moveToNextState();

        return "OK";
    }

    @Override
    public String move(List<ElementaryTokenMove> moves) throws InvalidMoveException {
        throwErrorIfRequestStateMismatch(GameState.VC_MOVEMENT_EXPECTED);
        throwErrorIfDiceNumberUnset();

        int stepsRequiredForMove = natureTokenSet.countStepsAndCheckIfLegal(phase, moves);
        if (stepsRequiredForMove != lastDiceRoll)
            throw new InvalidMoveException("this step requires a dice roll of " + stepsRequiredForMove);

        natureTokenSet.moveVC(phase, moves);

        moveToNextState();

        return "OK";
    }
}
