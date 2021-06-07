package server;

import clientAndServerPart.exceptions.IncorrectValueException;
import clientAndServerPart.exceptions.NullFieldException;


import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.Scanner;
import data.*;


/**
 * This class is for creating new instances of Worker class
 */
public class WorkerFactory {
    private Long id;
    private Scanner scanner;
    private boolean boolWF;
    private Object loadObject;
    public Long getId() {
        return id;
    }

    /**
     * @param startId - start point for id counter
     */
    public WorkerFactory(Long startId) {
        this.id = startId;
    }
    public void setScanner(Scanner scanner){
        this.scanner = scanner;
    }
    public Scanner getScanner(){
        return scanner;
    }
    public void setBoolean(boolean bo){
        boolWF = bo;
    }

    /**
     * Creates new worker with new id and creationDate
     * @param name - worker's name
     * @param coordinates - worker's coordinates
     * @param salary - worker's salary
     * @param startDate - worker's startDate
     * @param endDate - worker's endDate
     * @param position - worker's position
     * @param person - worker's height and weight
     * @return woker instance
     * @throws NullFieldException if field is null, when is shouldn't be null
     * @throws IncorrectValueException - if value of the field contains wrong data, which is not allowed in this field
     */
    public Worker createWorker(String name, Coordinates coordinates, double salary, ZonedDateTime startDate, ZonedDateTime endDate, Position position, Person person) throws NullFieldException, IncorrectValueException {
        return createWorkerWithIdAndCreationDate(++id, name, coordinates, new Date(), salary, startDate, endDate, position, person);
    }

    /**
     * Create worker with given id and creationDate
     * @param _id worker's id
     * @param name - worker's name
     * @param coordinates - worker's coordinates
     * @param excreationDate - worker's creationDate
     * @param salary - worker's salary
     * @param startDate - worker's startDate
     * @param endDate - worker's endDate
     * @param position - worker's position
     * @param person - worker's height and weight
     * @return worker instance
     * @throws NullFieldException if field is null, when is shouldn't be null
     * @throws IncorrectValueException - if value of the field contains wrong data, which is not allowed in this field
     */
    public Worker createWorkerWithIdAndCreationDate(Long _id, String name, Coordinates coordinates, Date excreationDate, double salary, ZonedDateTime startDate, ZonedDateTime endDate, Position position, Person person) throws NullFieldException, IncorrectValueException {
        if (name == null || name.length() == 0) {
            throw new NullFieldException("Name");
        }
        if (coordinates == null) {
            throw new NullFieldException("Coordinates");
        }

        if (salary <= 0) {
            throw new IncorrectValueException("Salary", "This field should be more than 0.");
        }
        if (startDate == null) {
            throw new NullFieldException("StartDate");
        }

        Instant instant = excreationDate.toInstant();
        ZonedDateTime creationDate = instant.atZone(ZoneId.systemDefault());

        return new Worker(_id, name, coordinates, creationDate, salary, startDate, endDate, position, person);
    }

    /**
     * read worker from console
     * @return worker instance
     * @throws NullFieldException if field is null, when is shouldn't be null
     * @throws IncorrectValueException - if value of the field contains wrong data, which is not allowed in this field
     */
    public Worker getWorkerFromConsole() throws NullFieldException, IncorrectValueException {
        String name;
        Long x;
        Integer y;
        Double salary;
        ZonedDateTime startDate;
        ZonedDateTime endDate;
        Position position;
        Long height;
        Integer weight;

        FieldChecker fieldChecker = new FieldChecker(scanner,boolWF);
        name = fieldChecker.readAndCheckName();
        x = fieldChecker.readAndCheckX();
        y = fieldChecker.readAndCheckY();
        salary = fieldChecker.readAndCheckSalary();
        startDate = fieldChecker.readAndCheckStartDate();
        endDate = fieldChecker.readAndCheckEndDate();
        position = fieldChecker.readAndCheckPos();
        height = fieldChecker.readAndCheckHeight();
        weight = fieldChecker.readAndCheckWeight();

        return createWorker(name, new Coordinates(x, y), salary, startDate, endDate, position, new Person(height, weight));
    }

    public void setLoadObject(Object loadObject){
        this.loadObject = loadObject;
    }
    public Object getLoadObject(){
        return loadObject;
    }

    /**
     * Set new start point for id counter
     * @param id1 - indicated start point for id counter
     */
    public void setStartId(long id1) {
        id = id1;
    }
}
