package server;

import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Print objects to console or read it with checking null value
 */
public class Console implements Runnable{
    private final Scanner scanner;

    public Console(Scanner scanner) {
        this.scanner = scanner;
    }

    /**
     * Prints toOut.toString() + \n to Console
     * @param toOut - Object ot print
     */
    public static void println(Object toOut) {
        System.out.println(toOut);
    }

    public String readln() {
        String line;
        try {
            line = scanner.nextLine();
        } catch (NoSuchElementException exception) {
            System.exit(0);
            line = null;
        }
        if (line.length() == 0) {
            line = null;
        }
        return line;
    }

    @Override
    public void run() {

    }
}
