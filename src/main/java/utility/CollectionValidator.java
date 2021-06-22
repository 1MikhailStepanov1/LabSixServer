package utility;

import data.Position;
import data.Worker;

import java.util.Arrays;
import java.util.LinkedList;

public class CollectionValidator {
    private final CollectionManager collectionManager;
    public CollectionValidator(CollectionManager collectionManager){
        this.collectionManager = collectionManager;
    }

    public static boolean validateObject(Worker worker){
        boolean temp = false;
        if (!worker.getName().equals("") && worker.getName() != null && worker.getCoordinates().getCoordinateX() <= 768 && worker.getCoordinates().getCoordinateY() != null && worker.getSalary() > 0 && worker.getStartDate() != null && checkExistPosition(worker.getPosition().toString()) && (worker.getPerson() == null || (worker.getPerson().getHeight()>0 && worker.getPerson().getWeight() >0))){
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

    private static boolean checkExistPosition(String position){
        return Arrays.stream(Position.values()).anyMatch(x->x.name().equals(position));
    }

    public boolean checkExistId(Long id){
        for (Worker worker : collectionManager.getCollection()){
            return worker.getId() == id;
        }
        return false;
    }
}
