package utility;

import data.Worker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import request.AnswerSender;
import request.RequestAcceptor;

import java.util.ArrayList;

public class Receiver {
    private static final Logger logger = LoggerFactory.getLogger(RequestAcceptor.class);
    private final CollectionManager collectionManager = new CollectionManager();
    private final CollectionValidator collectionValidator = new CollectionValidator(collectionManager);
    private final WorkerFactory workerFactory = new WorkerFactory();
    private final AnswerSender answerSender = new AnswerSender(logger);

    public Receiver() {

    }

    public void add() {
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (CollectionValidator.validateObject(worker)){
            collectionManager.add(worker);
            answerSender.addToAnswer("Worker was added to the collection.");
        } else {
            answerSender.addToAnswer("Received worker haven't passed validation.");
        }
        logger.info("Result of command \"add\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void addIfMax(){
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (CollectionValidator.validateObject(worker)){
            collectionManager.addIfMax(worker);
            answerSender.addToAnswer("Worker was added to the collection.");
        } else {
            answerSender.addToAnswer("Received worker haven't passed validation.");
        }
        logger.info("Result of command \"add_if_max\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void clear(){
        collectionManager.clear();
        answerSender.addToAnswer("Collection has been cleared.");
        logger.info("Result of command \"clear\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void countLessThanStartDate(String arg){
        String answer = collectionManager.countLessThanStartDate(arg);
        answerSender.addToAnswer(answer);
        logger.info("Result of command \"count_less_than_start_date\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void filterGreaterThanStartDate(String arg){
        ArrayList<Worker> answer = collectionManager.filterGreaterThanStartDate(arg);
        if (answer == null) {
            answerSender.addToAnswer("Collection doesn't contains satisfying elements.");
        } else {
            answer.forEach(answerSender::addToAnswer);
        }
        logger.info("Result of command \"filter_greater_than_start_date\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void groupCountingByPosition(){
        String answer = collectionManager.groupCountingByPosition();
        answerSender.addToAnswer(answer);
        logger.info("Result of command \"group_counting_by_position\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void info(){
        String[] answer = collectionManager.getInfo();
        for (int i = 0; i < answer.length; i++) {
            answerSender.addToAnswer(answer[i]);
        }
        logger.info("Result of command \"info\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeById(String arg){
        long id;
        try {
            id = Long.parseLong(arg);
            if (collectionValidator.checkExistId(id)) {
                collectionManager.removeById(id);
                answerSender.addToAnswer("Element with id " + id + " has been removed.");
            } else {
                answerSender.addToAnswer("There is no elements with matched id in the collection.");
            }
        } catch (NumberFormatException exception) {
            System.out.println(exception.getMessage());
        }
        logger.info("Result of command \"remove_by_id\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeGreater(){
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (CollectionValidator.validateObject(worker)) {
            if (collectionManager.removeGreater(worker)) {
                answerSender.addToAnswer("Elements have been removed.");
            } else {
                answerSender.addToAnswer("There is no elements in collection which are greater than indicated one.");
            }
        } else {
            answerSender.addToAnswer("Received element don`t pass validation.");
        }
        logger.info("Result of command \"remove_greater\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void removeLower(){
        Worker worker = (Worker) workerFactory.getLoadObject();
        if (CollectionValidator.validateObject(worker)) {
            if (collectionManager.removeLower(worker)) {
                answerSender.addToAnswer("Elements have been removed.");
            } else {
                answerSender.addToAnswer("There is no elements in collection which are greater than indicated one.");
            }
        } else {
            answerSender.addToAnswer("Received element don`t pass validation.");
        }
        logger.info("Result of command \"remove_lower\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void show(){
        answerSender.addToAnswer(collectionManager.show());
        logger.info("Result of command \"show\" has been sent to client.");
        answerSender.sendAnswer();
    }

    public void update(String arg){
        Long tempId;
        Worker worker = (Worker) workerFactory.getLoadObject();
        try {
            tempId = Long.parseLong(arg);
            if (collectionValidator.checkExistId(tempId)) {
                if (CollectionValidator.validateObject(worker)) {
                    collectionManager.update(tempId, worker);
                    answerSender.addToAnswer("Element has been updated.");
                } else {
                    answerSender.addToAnswer("Received element don't pass validation.");
                }
            } else {
                answerSender.addToAnswer("There is no elements with matched id in the collection.");
            }
        } catch (NumberFormatException exception) {
            answerSender.addToAnswer("Command is incorrect.");
        }
        logger.info("Result of command \"update\" has been sent to client.");
        answerSender.sendAnswer();
    }
}
