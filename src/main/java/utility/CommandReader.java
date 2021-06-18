package utility;

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is used for read commands from console and separate command name and its' arguments for invoker
 */
public class CommandReader {
    private final Scanner scanner;
    private final Invoker invoker;
    private final Pattern commandNamePattern;
    private final Pattern argPattern;

    /**
     * @param scanner - is used to read commands from console
     * @param invoker - invoker which wil execute received commands
     */
    public CommandReader(Scanner scanner, Invoker invoker) {
        this.scanner = scanner;
        this.invoker = invoker;
        commandNamePattern = Pattern.compile("^\\w+");
        argPattern = Pattern.compile("\\b(.*\\s*)*");
    }

    /**
     * Start reading loop
     * Loop reads commands and calls invoker
     * Loop is finished when input is empty or exit commands is called
     */
    public void activeMode() {
        String line;
        String command;
        String arg;
        do {
            try {
                line = scanner.nextLine();
            } catch (NoSuchElementException exception) {
                break;
            }
            Matcher matcher = commandNamePattern.matcher(line);
            if (matcher.find()){
                command = matcher.group();
            }else{
                System.out.println("Input is not a command.");
                continue;
            }
            line = line.substring(command.length());
            matcher = argPattern.matcher(line);
            if (matcher.find()){
                arg = matcher.group();
            } else {
                arg = "";
            }
            invoker.exe(command, arg);
        } while (!invoker.isStopRequested());
    }

}
