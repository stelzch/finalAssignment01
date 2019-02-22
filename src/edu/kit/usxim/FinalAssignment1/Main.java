package edu.kit.usxim.FinalAssignment1;

import edu.kit.informatik.Terminal;
import edu.kit.usxim.FinalAssignment1.exceptions.GameException;
import edu.kit.usxim.FinalAssignment1.exceptions.ProgramQuitRequestException;

import java.util.Scanner;

public class Main {
    /**
     * The Main Method
     * @param args command line arguments
     */
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        UserInputParser parser = new UserInputParser();

        while (true) {
            String line = Terminal.readLine();

            try {
                Terminal.printLine(parser.parseInput(line));
            } catch (ProgramQuitRequestException e) {
                break;
            } catch (GameException e) {
                Terminal.printError(e.getMessage());
            }
        }
    }
}
