package edu.kit.usxim.FinalAssignment1;

import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidDiceNumberException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidMoveException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidPlacementException;

import java.util.List;

/**
 * This interface specifies all the commands the user can enter
 */
public interface PlayerCommandExecutor {
    /**
     * Get the board game state at a specific field
     * @param pos the target position
     * @return the state at the given position
     */
    String state(Coordinates pos) throws InvalidCoordinatesException, InvalidCoordinatesException;

    /**
     * Get the board as string representation
     * @return ASCII representation of the game board
     */
    String print();

    /**
     * Set the vesta or ceres tokens on new positions
     * @param pos the target position
     * @return status message
     * @throws InvalidPlacementException if the placement was illegal
     * @throws IllegalAccessException if the target token is already occupied
     */
    String setVC(Coordinates pos) throws InvalidPlacementException, IllegalAccessException, InvalidPlacementException, InvalidCoordinatesException;

    /**
     * Signals that the dice has been rolled
     * @param symbol the symbol the dice shows
     * @return status message
     */
    String roll(String symbol) throws InvalidDiceNumberException;

    /**
     * Place a mission-control token on a field
     * @param start the starting coordinates
     * @param end the ending coordinates
     * @return status message
     * @throws InvalidPlacementException if the placement was illegal
     */
    String place(Coordinates start, Coordinates end) throws InvalidPlacementException, InvalidCoordinatesException;

    /**
     * Execute the provided elementary moves one after the other
     * @param moves a collection moves to execute
     * @throws InvalidMoveException if one of the moves was invalid
     * @return status message
     */
    String move(List<ElementaryTokenMove> moves) throws InvalidMoveException, InvalidCoordinatesException;
}
