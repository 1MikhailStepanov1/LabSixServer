package command;

import data.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import server.AnswerSender;
import server.WorkerFactory;
import server.utils.Checker;
import server.utils.CollectionManager;
import server.utils.CollectionValidator;
import server.utils.FileWorker;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;

public class CommandReceiver {
    private static final Logger logger = LoggerFactory.getLogger(CommandReceiver.class);
    private CollectionManager collectionManager = new CollectionManager();
    private Checker checker = new Checker(collectionManager);
    private CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
    private final FileWorker fileWorker = new FileWorker(collectionManager, collectionValidator);
    private final WorkerFactory workerFactory = new WorkerFactory(1L);
    private InetAddress address;
    private int port;
    private final AnswerSender answerSender = new AnswerSender(logger);


    public CommandReceiver(InetAddress address, int port) {
        this.address = address;
        this.port = port;
    }

    public void add()  {
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (Checker.checkWorker(worker)) {
            collectionManager.add(worker);
            answerSender.addToAnswer("Worker has been added to collection.");
        } else {
            answerSender.addToAnswer("Received element don`t pass validation.");
        }
        logger.info(String.format("Result of command add has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void addIfMax() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (Checker.checkWorker(worker)) {
            collectionManager.addIfMax(worker);
            answerSender.addToAnswer("Worker has been added to collection.");
        } else {
            answerSender.addToAnswer("Received element don`t pass validation.");
        }
        logger.info(String.format("Result of command add_if_max has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void clear()  {
        collectionManager.clear();
        answerSender.addToAnswer("Collection has been cleared.");
        logger.info(String.format("Result of command clear has been sent to the client %s:%s"), address, port);
    }

    public void countLessThanStartDate(String arg) {
        String answer = collectionManager.countLessThanStartDate(arg);
        answerSender.addToAnswer(answer);
        logger.info(String.format("Result of command count_less_than_start_date has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void filterGreaterThanStartDate(String arg) {
        ArrayList<Worker> answer = collectionManager.filterGreaterThanStartDate(arg);
        if (answer == null) {
            answerSender.addToAnswer("Collection doesn't contains satisfying elements.");
        } else {
            for (Worker worker : answer) {
                answerSender.addToAnswer(worker);
            }
        }
        logger.info(String.format("Result of command filter_greater_than_start_date has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void groupCountingByPosition() {
        String answer = collectionManager.groupCountingByPosition();
        answerSender.addToAnswer(answer);
        logger.info(String.format("Result of command group_counting_by_position has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void info() throws IOException {
        String[] answer = collectionManager.getInfo();
        for (int i = 0; i < answer.length; i++) {
            answerSender.addToAnswer(answer[i]);
        }
        logger.info(String.format("Result of command info has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void removeById(String arg)  {
        long id;
        try {
            id = Long.parseLong(arg);
            if (checker.checkExistId(id)) {
                collectionManager.removeById(id);
                answerSender.addToAnswer("Element with id " + id + " has been removed.");
            } else {
                answerSender.addToAnswer("There is no elements with matched id in the collection.");
            }
        } catch (NumberFormatException exception) {
            System.out.println(exception.getMessage());
        }
        logger.info(String.format("Result of command remove_by_id has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void removeGreater() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (Checker.checkWorker(worker)) {
            if (collectionManager.removeGreater(worker)) {
                answerSender.addToAnswer("Elements have been removed.");
            } else {
                answerSender.addToAnswer("There is no elements in collection which are greater than indicated one.");
            }
        } else {
            answerSender.addToAnswer("Received element don`t pass validation.");
        }
        logger.info(String.format("Result of command remove_greater has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void removeLower(){
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (Checker.checkWorker(worker)) {
            if (collectionManager.removeLower(worker)) {
                answerSender.addToAnswer("Elements have been removed.");
            } else {
                answerSender.addToAnswer("There is no elements in collection which are lower than indicated one.");
            }
        } else {
            answerSender.addToAnswer("Received element don`t pass validation.");
        }
        logger.info(String.format("Result of command remove_lower has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void show() {
        answerSender.addToAnswer(collectionManager.show());
        logger.info(String.format("Result of command show has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void update(String id) {
        Long tempId;
        Worker worker = (Worker) workerFactory.getLoadObject();
        try {
            tempId = Long.parseLong(id);
            if (checker.checkExistId(tempId)) {
                if (Checker.checkWorker(worker)) {
                    collectionManager.update(tempId, worker);
                    answerSender.addToAnswer("Element has been updated.");
                } else {
                    answerSender.addToAnswer("Element is not correct.");
                }
            } else {
                answerSender.addToAnswer("There is no elements with matched id in the collection.");
            }
        } catch (NumberFormatException exception) {
            answerSender.addToAnswer("Command is incorrect.");
        }
        logger.info(String.format("Result of command update has been sent to the client %s:%s"), address, port);
        answerSender.sendAnswer();
    }

    public void save() {
        fileWorker.getToXmlFormat();
    }
}
