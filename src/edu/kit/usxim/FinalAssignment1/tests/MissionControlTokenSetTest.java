package edu.kit.usxim.FinalAssignment1.tests;

import edu.kit.usxim.FinalAssignment1.MissionControlTokenSet;
import edu.kit.usxim.FinalAssignment1.PlayingTokenSet;
import edu.kit.usxim.FinalAssignment1.Token;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidDiceNumberException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MissionControlTokenSetTest {

    @Test
    public void checkStartingTokensContain2Through7() {
        PlayingTokenSet pts = new MissionControlTokenSet();

        List<Integer> sizes = new ArrayList();


        Iterator<Token> it = pts.iterator();
        while (it.hasNext()) {
            Token t = it.next();
            sizes.add(t.getSize());
        }

        Collections.sort(sizes);

        for (int i = 2; i <= 7; i++)
            assertTrue(sizes.contains(i));
    }

    @Test
    public void checkTokenProposalForDiceNumber() throws InvalidDiceNumberException {
        PlayingTokenSet pts;
        pts = new MissionControlTokenSet();
        List<Token> proposal = pts.getPossibleTokensForDiceNumber(5);

        List<Token> expected = new ArrayList();
        expected.add(new Token(Token.Type.MISSION_CONTROL, 5));

        assertIterableEquals(expected, proposal);
    }

    @Test
    public void checkTokenProposalMissingTokens() throws InvalidDiceNumberException {
        PlayingTokenSet pts = new MissionControlTokenSet();
        pts.removeToken(new Token(Token.Type.MISSION_CONTROL, 3));

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(Token.Type.MISSION_CONTROL, 2));
        expected.add(new Token(Token.Type.MISSION_CONTROL, 4));

        List<Token> proposal = pts.getPossibleTokensForDiceNumber(3);

        Collections.sort(expected);
        Collections.sort(proposal);

        System.out.println(pts);
        for (Token t : proposal) {
            System.out.println("Token is: " + t.getSize());
        }

        assertIterableEquals(expected, proposal);
    }

    @Test
    public void checkTokenProposalHigherLimit() throws InvalidDiceNumberException {
        PlayingTokenSet pts = new MissionControlTokenSet();
        pts.removeToken(new Token(Token.Type.MISSION_CONTROL, 7));

        List<Token> proposal = pts.getPossibleTokensForDiceNumber(7);

        List<Token> expected = new ArrayList<>();
        expected.add(new Token(Token.Type.MISSION_CONTROL, 6));

        assertIterableEquals(expected, proposal);
    }

    @Test
    public void checkProposalWhenNoTokensLeft() throws InvalidDiceNumberException {
        PlayingTokenSet pts = new MissionControlTokenSet();
        for (int i = 2; i <= 7; i++)
            pts.removeToken(new Token(Token.Type.MISSION_CONTROL, i));

        List<Token> expected = new ArrayList<>();
        List<Token> proposal = pts.getPossibleTokensForDiceNumber(3);

        assertIterableEquals(expected, proposal);
    }

    @Test
    public void checkTokenProposalDiceBounds() {
        Executable useLowDiceNumber = () -> {
            MissionControlTokenSet pts = new MissionControlTokenSet();

            pts.getPossibleTokensForDiceNumber(1);
        };

        Executable useHighDiceNumber = () -> {
            MissionControlTokenSet pts = new MissionControlTokenSet();

            pts.getPossibleTokensForDiceNumber(Integer.MAX_VALUE - 3);
        };

        Executable useNegativeDiceNumber = () -> {
            MissionControlTokenSet pts = new MissionControlTokenSet();

            pts.getPossibleTokensForDiceNumber(-555);
        };

        assertThrows(InvalidDiceNumberException.class, useLowDiceNumber);
        assertThrows(InvalidDiceNumberException.class, useHighDiceNumber);
        assertThrows(InvalidDiceNumberException.class, useNegativeDiceNumber);
    }
}