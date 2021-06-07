package server;

import data.Position;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class FieldChecker {
    private final Scanner scanner;
    private boolean bool;

    public FieldChecker(Scanner scanner, boolean bool) {
        this.scanner = scanner;
        this.bool = bool;
    }


    public <T> T readAndCheckField(String FieldName, String error, FieldCheckerHelp<T> rule) {
        T temp;
        while (true) {
            if (bool) {
                System.out.println("Enter worker`s " + FieldName + ":");
            }
            try {
                Console console = new Console(scanner);
                temp = rule.check(console.readln());
            } catch (NumberFormatException exception) {
                System.out.println("Input is incorrect. Please, try again." + error);
                continue;
            } catch (NullPointerException exception) {
                System.out.println("Value of this field can`t be null. Please try again.");
                continue;
            } catch (DateTimeParseException exception) {
                System.out.println("Format of input is incorrect. Use dd.mm.yyyy hh:mm:ss +/-hh:mm");
                continue;
            } catch (IllegalArgumentException exception) {
                System.out.println("Input doesn't contain allowed positions. Please, try again.");
                continue;
            } catch (NoSuchElementException exception) {
                continue;
            }
            return temp;
        }
    }


    public String readAndCheckName() throws NumberFormatException {
        FieldCheckerHelp<String> tempInterface = str -> {
            if (str == null || str.equals("")) {
                throw new NumberFormatException();
            }
            return str;
        };
        return readAndCheckField("name", "", tempInterface);
    }

    public Long readAndCheckX() throws NumberFormatException {
        FieldCheckerHelp<Long> tempInterface = str -> {
            long result = Long.parseLong(str);
            if (result > 768) {
                throw new NumberFormatException();
            }
            return result;
        };
        return readAndCheckField("coordinate X", "(Reminder: Coordinate X can't be more than 768.)", tempInterface);
    }

    public Integer readAndCheckY() {
        FieldCheckerHelp<Integer> tempInterface = Integer::parseInt;
        return readAndCheckField("coordinate Y", "", tempInterface);
    }

    public Double readAndCheckSalary() throws NumberFormatException {
        FieldCheckerHelp<Double> tempInterface = str -> {
            double result = Double.parseDouble(str);
            if (result <= 0) {
                throw new NumberFormatException();
            }
            return result;
        };
        return readAndCheckField("salary", "(Reminder: Salary should be more than 0.)", tempInterface);
    }

    public ZonedDateTime readAndCheckStartDate() {
        FieldCheckerHelp<ZonedDateTime> tempInterface = str -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
            return ZonedDateTime.parse(str, formatter);
        };
        return readAndCheckField("start date", "", tempInterface);
    }

    public ZonedDateTime readAndCheckEndDate() {
        FieldCheckerHelp<ZonedDateTime> tempInterface = str -> {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
            if (str == null) {
                return null;
            } else {
                return ZonedDateTime.parse(str, formatter);
            }
        };
        return readAndCheckField("end date", "", tempInterface);
    }

    public Position readAndCheckPos() {
        if (bool) {
            for (Position pos : Position.values()) {
                System.out.println(pos.toString());
            }
        }
        FieldCheckerHelp<Position> tempInterface = str -> {
            if (str == null) {
                return null;
            } else {
                return Position.valueOf(str.toUpperCase());
            }
        };
        return readAndCheckField("position", "", tempInterface);
    }

    public Long readAndCheckHeight() throws NumberFormatException {
        FieldCheckerHelp<Long> tempInterface = str -> {
            long result = Long.parseLong(str);
            if (result <= 0) {
                throw new NumberFormatException();
            }
            return result;
        };
        return readAndCheckField("height", "(Reminder: Height should be more than 0.)", tempInterface);
    }

    public Integer readAndCheckWeight() throws NumberFormatException {
        FieldCheckerHelp<Integer> tempInterface = str -> {
            int result = Integer.parseInt(str);
            if (result <= 0) {
                throw new NumberFormatException();
            }
            return result;
        };
        return readAndCheckField("weight", "(Reminder: Weight should be more than 0.)", tempInterface);
    }
}