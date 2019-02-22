package edu.kit.usxim.FinalAssignment1;

import edu.kit.usxim.FinalAssignment1.exceptions.*;

import java.util.List;

/**
 * This interface specifies all the commands the user can enter
 */
public interface PlayerCommandExecutor {
    /**
     * Get the board game state at a specific field
     * @param pos the target position
     * @return the state at the given position
     * @throws InvalidCoordinatesException if the target field coordinates are incorrect
     */
    String state(Coordinates pos) throws InvalidCoordinatesException;

    /**
     * Get the board as string representation
     * @return ASCII representation of the game board
     */
    String print();

    /**
     * Set the vesta or ceres tokens on new positions
     * @param pos the target position
     * @return status message
     * @throws GameException if the provided coordinates were incorrect
     */
    String setVC(Coordinates pos) throws GameException;

    /**
     * Signals that the dice has been rolled
     * @param symbol the symbol the dice shows
     * @return status message
     * @throws InvalidDiceNumberException if the provided dice number was invalid
     * @throws InvalidCommandException if the command was not expected by the game
     */
    String roll(String symbol) throws InvalidDiceNumberException, InvalidCommandException;

    /**
     * Place a mission-control token on a field
     * @param start the starting coordinates
     * @param end the ending coordinates
     * @return status message
     * @throws GameException if either the placement was illegal, the command unexpected or the coordinates invalid
     */
    String place(Coordinates start, Coordinates end) throws GameException;

    /**
     * Execute the provided elementary moves one after the other
     * @param moves a collection moves to execute
     * @return status message
     * @throws GameException if something goes wrong
     */
    String move(List<ElementaryTokenMove> moves) throws GameException;
}
