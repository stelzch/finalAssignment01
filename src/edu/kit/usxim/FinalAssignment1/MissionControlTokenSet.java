package edu.kit.usxim.FinalAssignment1;


import java.util.*;

public class MissionControlTokenSet implements PlayingTokenSet {
    /** The smallest token this set contains */
    public static final int MINIMUM_TOKEN_SIZE = 2;
    /** The largest token this set contains */
    public static final int MAXIMUM_TOKEN_SIZE = 7;

    private SortedSet<Token> availableTokens;

    /**
     * Default constructor
     */
    public MissionControlTokenSet() {
        availableTokens = new TreeSet();

        for (int i = MINIMUM_TOKEN_SIZE; i <= MAXIMUM_TOKEN_SIZE; i++) {
            availableTokens.add(new Token(Token.Type.MISSION_CONTROL, i));
        }
    }

    private void throwErrorIfDiceNumberIncorrect(int diceNumber) throws InvalidDiceNumberException {
        if (diceNumber < MINIMUM_TOKEN_SIZE)
            throw new InvalidDiceNumberException("dice number was to small");

        if (diceNumber > MAXIMUM_TOKEN_SIZE)
            throw new InvalidDiceNumberException("dice number was to large");
    }

    public Token getNthTokenOfSet(Set<Token> set, int n) {
        int index = 0;
        for (Token t : set) {
            if (index == n)
                return t;

            index++;
        }

        throw new IndexOutOfBoundsException();
    }

    /**
     * Search for the next lower token and add it to the given list if it exists
     * @param searchToken the token to search for
     */
    private void addNextLowerTokenIfExistent(Token searchToken, List<Token> result) {
        Token lastTokenLowerThanSearch = null;

        for (Token currentToken : availableTokens) {
            boolean currentTokenisLowerThanSearch = currentToken.compareTo(searchToken) == -1;
            if (currentTokenisLowerThanSearch)
                lastTokenLowerThanSearch = currentToken;
        }

        if (lastTokenLowerThanSearch != null)
            result.add(lastTokenLowerThanSearch);

        System.out.println("LowerToken = " + lastTokenLowerThanSearch);
    }

    /**
     * Search for the next lower token and add it to the given list if it exists
     * @param searchToken the token to search for
     * @param result the list the next lower token should be added to if found
     */
    private void addNextHigherTokenIfExistent(Token searchToken, List<Token> result) {
        for (Token currentToken : availableTokens) {
            boolean currentTokenisBiggerThanSearch = currentToken.compareTo(searchToken) == 1;
            if (currentTokenisBiggerThanSearch) {
                System.out.println("token+" + currentToken.getSize());
                result.add(currentToken);
                return;
            }
        }
    }

    /**
     * Get the possible tokens for a given dice number
     * @param diceNumber the number of the dice (2...7)
     * @return a list of usable tokens
     * @throws InvalidDiceNumberException if the given dice number is invalid
     */
    @Override
    public List<Token> getPossibleTokensForDiceNumber(int diceNumber) throws InvalidDiceNumberException {
        throwErrorIfDiceNumberIncorrect(diceNumber);

        List<Token> possibleTokens = new ArrayList<Token>();

        Token searchToken = new Token(Token.Type.MISSION_CONTROL, diceNumber);
        System.out.println("searchTokenSize: " + searchToken.getSize());

        if (availableTokens.contains(searchToken)) {
            possibleTokens.add(searchToken);
            return possibleTokens;
        }

        addNextHigherTokenIfExistent(searchToken, possibleTokens);
        addNextLowerTokenIfExistent(searchToken, possibleTokens);
        return possibleTokens;
    }

    @Override
    public void removeToken(Token removalToken) {
        if (availableTokens.contains(removalToken))
            availableTokens.remove(removalToken);
    }


    /**
     * Get the iterator
     * @return an iterator of the token this class stores
     */
    @Override
    public Iterator<Token> iterator() {
        return availableTokens.iterator();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (Token t : availableTokens) {
            sb.append(t.getSize());
            sb.append(" ");
        }

        return sb.toString().trim();
    }

}
