import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class MainClass {
    public static void main(String[] args) throws IOException  {
        BufferedReader sysIn = new BufferedReader(new InputStreamReader(System.in, "UTF-8"));

        String line;
        System.out.println("Hello World!");
        while ((line = sysIn.readLine()) != null) {
            System.out.print("Input was: " + line + "\n");
            StringBuilder reversed = new StringBuilder();
            for (int i = line.length() - 1; i >= 0; i--)
                reversed.append(line.charAt(i));

            System.out.println(reversed);
        }
        System.out.println("Program quitting");
    }
}