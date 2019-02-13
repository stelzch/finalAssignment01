package edu.kit.usxim.FinalAssignment1;


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

    private void moveTokenToNewPosition(Game.GamePhase phase, int newX, int newY) throws InvalidPlacementException {
        if (phase == Game.GamePhase.PHASE_ONE) {
            board.moveToken(vestaX, vestaY, newX, newY);
        } else {
            board.moveToken(ceresX, ceresY, newX, newY);
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

    /**
     * Move or place the Vesta/Ceres token at the given coordinates
     * @param phase the phase the game is currently in
     * @param x the x coordinate
     * @param y the y coordinate
     * @throws InvalidPlacementException if the coordinates are incorrect or the target coordinates already occupied
     */
    public void placeVC(Game.GamePhase phase, int x, int y) throws InvalidPlacementException{
        if (hasTokenAlreadyBeenPlaced(phase)) {
            // We try to move the token
            moveTokenToNewPosition(phase, x, y);
        } else {
            Token relevantToken = (phase == Game.GamePhase.PHASE_ONE) ? vesta : ceres;
            board.placeToken(relevantToken, x, y, Token.Orientation.VERTICAL);
        }

        updateCoordinatesToNewPosition(phase, x, y);
    }
}
