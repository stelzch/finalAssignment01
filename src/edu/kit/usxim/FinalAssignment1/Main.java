package edu.kit.usxim.FinalAssignment1;

import edu.kit.informatik.Terminal;
import edu.kit.usxim.FinalAssignment1.exceptions.GameException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidCoordinatesException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidDiceNumberException;
import edu.kit.usxim.FinalAssignment1.exceptions.InvalidMoveException;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserInputParser parser = new UserInputParser();

        while(sc.hasNext()) {
            String line = sc.nextLine();

            try {
                Terminal.printLine(parser.parseInput(line));
            } catch (GameException e) {
                Terminal.printError(e.getMessage());
            }
        }
    }
}
