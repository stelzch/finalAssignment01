package edu.kit.usxim.FinalAssignment1;

/**
 * This class brings together all the other components
 */
public class Game implements PlayerCommandExecutor {
    public enum RoundState {
        /** The next move has to be a dice roll */
        DICE_ROLL_EXPECTED,
        /** The next move has to be a token placement */
        TOKEN_PLACEMENT_EXPECTED,
        /** The next move has to be the placement of vesta or ceres */
        VC_MOVEMENT_EXPECTED
    }

    public enum Phase {
        /** The game is in the first (vesta) phase */
        PHASE_ONE,
        /** The game is in the second (ceres) phase */
        PHASE_TWO
    }

    private MissionControlTokenSet tokensForPhaseOne;
    private MissionControlTokenSet tokensForPhaseTwo;
    private NatureTokenSet natureTokenSet;
    private Board board;


    private RoundState state;
    private Phase phase;

    /**
     * Default constructor
     */
    public Game() {
        tokensForPhaseOne = new MissionControlTokenSet();
        tokensForPhaseTwo = new MissionControlTokenSet();
        natureTokenSet = new NatureTokenSet();
        board = new Board();

        // At the start, we are in phase one and the VC must be placed
        phase = Phase.PHASE_ONE;
        state = RoundState.VC_MOVEMENT_EXPECTED;
    }

    /**
     * If the current state does not match the expectedStateForCommand, it throws an exception
     * @param expectedStateForCommand the state the game should be in in order for the command to be run properly
     */
    private void throwErrorIfRequestStateMismatch(RoundState expectedStateForCommand) {
        if (expectedStateForCommand != state) {
            throw new IllegalArgumentException("this command was not expected");
        }
    }


    /**
     * @return the state the game is currently in
     */
    public RoundState getState() {
        return state;
    }
    /**
     * Move the game to the next state
     */
    public void moveToNextState() {
        int currentStateIndex = state.ordinal();
        int nextStateIndex = (currentStateIndex + 1) % RoundState.values().length;

        state = RoundState.values()[nextStateIndex];
    }

    private void tryMoveVCToCoords(int dstX, int dstY) throws IllegalAccessException, InvalidPlacementException {
        if (!natureTokenSet.coordinatesAlreadySet(phase)) {
            // This is the first time VC is placed. Add the token to the game board
            board.placeToken(natureTokenSet.getCurrentToken(phase), dstX, dstY, Token.Orientation.VERTICAL);

            natureTokenSet.setCurrentX(dstX, phase);
            natureTokenSet.setCurrentY(dstY, phase);
        } else {
            int srcX = natureTokenSet.getCurrentX(phase);
            int srcY = natureTokenSet.getCurrentY(phase);

            board.moveToken(srcX, srcY, dstX, dstY);
        }
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
        throwErrorIfRequestStateMismatch(RoundState.VC_MOVEMENT_EXPECTED);

        tryMoveVCToCoords(n, m);
        moveToNextState();

        return "OK";
    }

    @Override
    public String roll(String symbol) {
        throwErrorIfRequestStateMismatch(RoundState.DICE_ROLL_EXPECTED);
        return null;
    }

    @Override
    public String place(int x1, int y1, int x2, int y2) {
        throwErrorIfRequestStateMismatch(RoundState.TOKEN_PLACEMENT_EXPECTED);
        return null;
    }
}
