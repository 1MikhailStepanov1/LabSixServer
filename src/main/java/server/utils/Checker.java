package server.utils;

import data.Position;
import data.Worker;

import java.util.Arrays;

public class Checker {
    private final CollectionManager collectionManager;
    public Checker(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }
    private static boolean checkExistPosition(String position){
        return Arrays.stream(Position.values()).anyMatch(x->x.name().equals(position));
    }

    public static boolean checkWorker(Worker worker){
        return (worker.getId() != 0) && (worker.getName() != null) && (!worker.getName().equals("")) && (worker.getCoordinates().getX() <= 768) && (worker.getCoordinates().getY() != null) && (worker.getCreationDate() != null) && (worker.getSalary() > 0) && (worker.getStartDate() != null) && (worker.getPosition() == null || checkExistPosition(worker.getPosition().toString())) && (worker.getPerson() == null || (worker.getPerson().getHeight() > 0 && worker.getPerson().getWeight() > 0));
    }

    public boolean checkExistId(Long id){
        for (Worker worker : collectionManager.getCollection()){
            return worker.getId() == id;
        }
        return false;
    }
}
