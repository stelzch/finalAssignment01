package edu.kit.usxim.FinalAssignment1;

/**
 * This class brings together all the other components
 */
public class Game implements PlayerCommandExecutor {
    public enum GameState {
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

    /**
     * Default constructor
     */
    public Game() {
        tokensForPhaseOne = new MissionControlTokenSet();
        tokensForPhaseTwo = new MissionControlTokenSet();
        board = new Board();

        // At the start, we are in phase one and the VC must be placed
        phase = GamePhase.PHASE_ONE;
        state = GameState.VC_MOVEMENT_EXPECTED;
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
    }

    private int parseDiceSymbol(String symbol) {
        if (symbol.matches("[0-9]")) {
            return Integer.parseInt(symbol);
        } else if (symbol.equals("DAWN")) {
            return 7;
        } else {
            throw new IllegalArgumentException("the dice symbol is invalid");
        }

    }

    @Override
    public String state(int m, int n) {
        char field = board.getTokenAt(n, m);
        return String.valueOf(field);
    }

    @Override
    public String print() {
        return null;
    }

    @Override
    public String setVC(int m, int n) throws InvalidPlacementException, IllegalAccessException {
        throwErrorIfRequestStateMismatch(GameState.VC_MOVEMENT_EXPECTED);

        moveToNextState();

        return "OK";
    }

    @Override
    public String roll(String symbol) {
        throwErrorIfRequestStateMismatch(GameState.DICE_ROLL_EXPECTED);

        return null;
    }

    @Override
    public String place(int x1, int y1, int x2, int y2) {
        throwErrorIfRequestStateMismatch(GameState.TOKEN_PLACEMENT_EXPECTED);
        return null;
    }
}
