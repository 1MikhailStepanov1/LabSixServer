package utility;

import data.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.AnswerSender;
import request.RequestAcceptor;

import java.util.Comparator;
import java.util.LinkedList;

public class Receiver {
    private static final Logger logger = LoggerFactory.getLogger(RequestAcceptor.class);
    private CollectionManager collectionManager;
    private final CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
    private final WorkerFactory workerFactory = new WorkerFactory();
    private final AnswerSender answerSender = new AnswerSender(logger);

    public Receiver(CollectionManager collectionManager) {
        this.collectionManager = collectionManager;

    }

    public void add() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (CollectionValidator.validateObject(worker)) {
            collectionManager.add(worker);
            answerSender.addToAnswer(true, "Worker was added to the collection.", null, null);
        } else {
            answerSender.addToAnswer(false, "Received worker haven't passed validation.", null, null);
        }
        logger.info("Result of command \"add\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void addIfMax() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (CollectionValidator.validateObject(worker)) {
            collectionManager.addIfMax(worker);
            answerSender.addToAnswer(true, "Worker was added to the collection.", null, null);
        } else {
            answerSender.addToAnswer(false, "Received worker haven't passed validation.", null, null);
        }
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
        Integer answer = collectionManager.countLessThanStartDate(arg);
        answerSender.addToAnswer(true, "Suitable elements in the collection: ", answer, null);
        logger.info("Result of command \"count_less_than_start_date\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void filterGreaterThanStartDate(String arg) {
        LinkedList<Worker> answer = collectionManager.filterGreaterThanStartDate(arg);
        answer.sort(Comparator.comparing(Worker::getName));
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
        answerSender.addToAnswer(true, answer,null ,null);
        logger.info("Result of command \"info\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeById(String arg) {
        long id;
        try {
            id = Long.parseLong(arg);
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
        if (CollectionValidator.validateObject(worker)) {
            if (collectionManager.removeGreater(worker)) {
                answerSender.addToAnswer(true,"Elements have been removed.",null, null);
            } else {
                answerSender.addToAnswer(false, "There is no elements in collection which are greater than indicated one.", null, null);
            }
        } else {
            answerSender.addToAnswer(false,"Received element don`t pass validation.", null, null);
        }
        logger.info("Result of command \"remove_greater\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeLower() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (CollectionValidator.validateObject(worker)) {
            if (collectionManager.removeLower(worker)) {
                answerSender.addToAnswer(true, "Elements have been removed.", null, null);
            } else {
                answerSender.addToAnswer(false,"There is no elements in collection which are greater than indicated one.", null, null);
            }
        } else {
            answerSender.addToAnswer(false,"Received element don`t pass validation.", null, null);
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
            if (collectionValidator.checkExistId(tempId)) {
                if (CollectionValidator.validateObject(worker)) {
                    collectionManager.update(tempId, worker);
                    answerSender.addToAnswer(true, "Element has been updated.", null,null);
                } else {
                    answerSender.addToAnswer(false,"Received element don't pass validation.", null,null);
                }
            } else {
                answerSender.addToAnswer(false,"There is no elements with matched id in the collection.", null,null);
            }
        } catch (NumberFormatException exception) {
            answerSender.addToAnswer(false,"Command is incorrect.", null, null);
        }
        logger.info("Result of command \"update\" has been sent to client.");
        answerSender.sendAnswer();
    }
}
