package edu.kit.usxim.FinalAssignment1;


public class NatureTokenSet {
    private Token vesta;
    private Token ceres;
    private int ceresX;
    private int ceresY;
    private int vestaX;
    private int vestaY;


    /**
     * Default constructor
     */
    public NatureTokenSet() {
        vesta = new Token(Token.Type.VESTA, Token.VESTA_OR_CERES_SIZE);
        ceres = new Token(Token.Type.CERES, Token.VESTA_OR_CERES_SIZE);
        ceresX = -1;
        ceresY = -1;
        vestaX = -1;
        vestaY = -1;
    }

    private void throwErrorIfCoordinateUnset(int coord) throws IllegalAccessException {
        if (coord == -1)
            throw new IllegalAccessException("the token must be placed first");
    }

    /**
     * Get the position for a given game state
     * @param phase the phase the game is currently in
     */
    public int getCurrentX(Game.Phase phase) throws IllegalAccessException {
        if (phase == Game.Phase.PHASE_ONE) {
            throwErrorIfCoordinateUnset(vestaX);
            return vestaX;
        } else {
            throwErrorIfCoordinateUnset(ceresX);
            return ceresX;
        }
    }
    /**
     * Get the position for a given game state
     * @param phase the phase the game is currently in
     */
    public int getCurrentY(Game.Phase phase) throws IllegalAccessException {
        if (phase == Game.Phase.PHASE_ONE) {
            throwErrorIfCoordinateUnset(vestaY);
            return vestaY;
        } else {
            throwErrorIfCoordinateUnset(ceresY);
            return ceresY;
        }
    }

    /**
     * Set the position according for the current state
     * @param x the new x-coord
     * @param phase the phase the game is currently in
     */
    public void setCurrentX(int x, Game.Phase phase) {
        if (phase == Game.Phase.PHASE_ONE) {
            vestaX = x;
        } else {
            ceresX = x;
        }
    }

    /**
     * Set the position according for the current state
     * @param y the new y-coord
     * @param phase the phase the game is currently in
     */
    public void setCurrentY(int y, Game.Phase phase) {
        if (phase == Game.Phase.PHASE_ONE) {
            vestaY = y;
        } else {
            ceresY = y;
        }
    }

    /**
     * Get the correct token for a given phase
     * @param phase the phase the game is currently in
     * @return a token that matches the game phase
     */
    public Token getCurrentToken(Game.Phase phase) {
        if (phase == Game.Phase.PHASE_ONE) {
            return vesta;
        } else {
            return ceres;
        }
    }

    /**
     * Check whether the coordinates have been set before
     * @param phase the phase the game is currently in
     */
    public boolean coordinatesAlreadySet(Game.Phase phase) {
        if (phase == Game.Phase.PHASE_ONE) {
            return (vestaX != -1 && vestaY != -1);
        } else {
            return (ceresX != -1 && ceresY != -1);
        }
    }
}
