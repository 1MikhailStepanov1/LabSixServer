package utility;

import com.sun.corba.se.spi.orbutil.threadpool.Work;
import data.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.AnswerSender;
import request.RequestAcceptor;

import java.util.LinkedList;

public class Receiver {
    private static final Logger logger = LoggerFactory.getLogger(RequestAcceptor.class);
    private final CollectionManager collectionManager;
    private final WorkerFactory workerFactory;
    private final AnswerSender answerSender;

    public Receiver(CollectionManager collectionManager, AnswerSender answerSender, WorkerFactory workerFactory) {
        this.collectionManager = collectionManager;
        this.answerSender = answerSender;
        this.workerFactory = workerFactory;

    }

    public void add() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        worker.setId(workerFactory.getId() + 1);
        workerFactory.setStartId(worker.getId());
        collectionManager.add(worker);
        answerSender.addToAnswer(true, "Worker was added to the collection.", null, null);
        logger.info("Result of command \"add\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void addIfMax() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        worker.setId(workerFactory.getId() + 1);
        workerFactory.setStartId(worker.getId());
        collectionManager.addIfMax(worker);
        answerSender.addToAnswer(true, "Worker was added to the collection.", null, null);
        logger.info("Result of command \"add_if_max\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void clear() {
        collectionManager.clear();
        answerSender.addToAnswer(true, "Collection has been cleared.", null, null);
        logger.info("Result of command \"clear\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void countLessThanStartDate(String arg) {
        Long answer = collectionManager.countLessThanStartDate(arg);
        answerSender.addToAnswer(true, "Suitable elements in the collection: ", answer, null);
        logger.info("Result of command \"count_less_than_start_date\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void filterGreaterThanStartDate(String arg) {
        LinkedList<Worker> answer = collectionManager.filterGreaterThanStartDate(arg);
        if (answer == null) {
            answerSender.addToAnswer(false, "Collection doesn't contains satisfying elements.", null, null);
        } else {
            answerSender.addToAnswer(true, "Command has done successfully.", null, answer);
        }
        logger.info("Result of command \"filter_greater_than_start_date\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void groupCountingByPosition() {
        String answer = collectionManager.groupCountingByPosition();
        answerSender.addToAnswer(true, answer, null, null);
        logger.info("Result of command \"group_counting_by_position\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void info() {
        String answer = collectionManager.getInfo();
        answerSender.addToAnswer(true, answer, null, null);
        logger.info("Result of command \"info\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeById(String arg) {
        long id;
        try {
            id = Long.parseLong(arg);
            CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
            if (collectionValidator.checkExistId(id)) {
                collectionManager.removeById(id);
                answerSender.addToAnswer(true, "Element with id " + id + " has been removed.", null, null);
            } else {
                answerSender.addToAnswer(false, "There is no elements with matched id in the collection.", null, null);
            }
        } catch (NumberFormatException exception) {
            System.out.println(exception.getMessage());
        }
        logger.info("Result of command \"remove_by_id\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeGreater() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (collectionManager.removeGreater(worker)) {
            answerSender.addToAnswer(true, "Elements have been removed.", null, null);
        } else {
            answerSender.addToAnswer(false, "There is no elements in collection which are greater than indicated one.", null, null);
        }
        logger.info("Result of command \"remove_greater\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeLower() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (collectionManager.removeLower(worker)) {
            answerSender.addToAnswer(true, "Elements have been removed.", null, null);
        } else {
            answerSender.addToAnswer(false, "There is no elements in collection which are greater than indicated one.", null, null);
        }
        logger.info("Result of command \"remove_lower\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void show() {
        answerSender.addToAnswer(true, "", null, collectionManager.show());
        logger.info("Result of command \"show\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void update(String arg) {
        Long tempId;
        Worker worker = (Worker) workerFactory.getLoadObject();
        try {
            tempId = Long.parseLong(arg);
            CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
            if (collectionValidator.checkExistId(tempId)) {
                collectionManager.update(tempId, worker);
                answerSender.addToAnswer(true, "Element has been updated.", null, null);
            } else {
                answerSender.addToAnswer(false, "There is no elements with matched id in the collection.", null, null);
            }
        } catch (NumberFormatException exception) {
            answerSender.addToAnswer(false, "Command is incorrect.", null, null);
        }
        logger.info("Result of command \"update\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void validateId(String arg) {
        Long tempId;
        try {
            tempId = Long.parseLong(arg);
            CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
            answerSender.addToAnswer(collectionValidator.checkExistId(tempId), "", null, null);
        } catch (NumberFormatException exception) {
            answerSender.addToAnswer(false, "", null, null);
        }
        logger.info("Result of validation has been sent to client.");
        answerSender.sendAnswer();
    }
}
