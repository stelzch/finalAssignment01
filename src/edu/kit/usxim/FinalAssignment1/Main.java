package edu.kit.usxim.FinalAssignment1;

import edu.kit.informatik.Terminal;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserInputParser parser = new UserInputParser();

        while(sc.hasNext()) {
            String line = sc.nextLine();

            try {
                Terminal.printLine(parser.parseInput(line));
            } catch (InvalidMoveException | IllegalArgumentException e) {
                Terminal.printError( e.getMessage());
            }
        }
    }
}
