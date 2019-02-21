package edu.kit.usxim.FinalAssignment1;

import edu.kit.usxim.FinalAssignment1.exceptions.InvalidDiceNumberException;

import java.util.Iterator;
import java.util.List;

/**
 * This interface specifies how a set of playing tokens is stored and can be handled
 */
public interface PlayingTokenSet {
    /**
     * Get the tokens currently in the set that fit the dice number
     *
     * @param diceNumber the number of the dice (2...7)
     * @return a list of tokens that are possible to set with that dice number
     * @throws InvalidDiceNumberException if the dice number was incorrect
     */
    List<Token> getPossibleTokensForDiceNumber(int diceNumber) throws InvalidDiceNumberException;

    /**
     * Remove a token of the token set
     *
     * @param removalToken the token to remove
     */
    void removeToken(Token removalToken);

    /**
     * Get an iterator to iterate over the tokens currently in the token set
     *
     * @return an Iterator<Token>
     */
    Iterator<Token> iterator();


}
