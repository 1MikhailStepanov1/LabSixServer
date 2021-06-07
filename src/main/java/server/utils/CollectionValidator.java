package server.utils;

import data.Worker;

import java.util.LinkedList;

public class CollectionValidator {
    private final CollectionManager collectionManager;
    public CollectionValidator(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    public static boolean validateObject(Worker worker){
        boolean temp = false;
        if (!worker.getName().equals("") && worker.getName() != null && worker.getCoordinates().getX() <= 768 && worker.getCoordinates().getY() != null && worker.getSalary() > 0 && worker.getStartDate() != null && (worker.getPerson() == null || (worker.getPerson().getHeight()>0 && worker.getPerson().getWeight() >0))){
            temp = true;
        }
        return temp;
    }

    public boolean validateCollection(LinkedList<Worker> collection){
        int temp = 0;
        for (Worker worker : collection){
            if (validateObject(worker)){
                temp+=1;
            }
        }
        return temp == collection.size();
    }
}
