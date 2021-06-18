package utility;

import data.Position;
import data.Worker;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;


/**
 * This class is used to do all operations with collection
 */
public class CollectionManager {
    private LinkedList<Worker> collection;
    private boolean ExeDone;
    private final ZonedDateTime InitTime;

    public CollectionManager() {
        collection = new LinkedList<>();
        InitTime = ZonedDateTime.now();
    }

    /**
     * Adds new worker to the collection
     * @param worker worker instance to be add
     */
    public void add(Worker worker) {
        ExeDone = true;
        collection.add(worker);
    }

    public void addIfMax(Worker worker){
        try {
            ExeDone = true;
            Worker max;
            max = collection.getFirst();
            for (Worker worker1 : collection) {
                if (max.compareTo(worker1) > 0) {
                    max = worker1;
                }
            }
            if (worker.compareTo(max) > 0) {
                collection.add(worker);
            }
        } catch (NullPointerException exception) {
            System.out.println(exception.getMessage());
        }
    }

    public String countLessThanStartDate(String arg){
        ZonedDateTime tempTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
        try {
            tempTime = ZonedDateTime.parse(arg, formatter);
        } catch (DateTimeParseException exception) {
            System.out.println(exception.getMessage());
        }
        String result = "";
        int count = 0;
        if (collection.size() > 0) {
            for (Worker worker : collection) {
                if (worker.getStartDate().compareTo(tempTime) < 0) {
                    count++;
                }
            }
            result = "Result: " + count;
        } else {
            result = "Collection is empty.";
        }
        return result;
    }

    public ArrayList<Worker> filterGreaterThanStartDate(String arg){
        ZonedDateTime tempTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
        try {
            tempTime = ZonedDateTime.parse(arg, formatter);
        } catch (DateTimeParseException exception) {
            System.out.println(exception.getMessage());
        }
        ArrayList<Worker> result = new ArrayList<>();
        if (collection.size() > 0) {
            for (Worker tempWorker : collection) {
                if (tempWorker.getStartDate().compareTo(tempTime) > 0) {
                    result.add(tempWorker);
                }
            }
        } else {
            result = null;
        }
        return result;
    }

    public String groupCountingByPosition(){
        String result = null;
        EnumMap<Position, Integer> enumMap = new EnumMap<>(Position.class);
        int s;
        enumMap.put(Position.MANAGER, 0);
        enumMap.put(Position.LABORER, 0);
        enumMap.put(Position.LEAD_DEVELOPER, 0);
        enumMap.put(Position.BAKER, 0);
        enumMap.put(Position.MANAGER_OF_CLEANING, 0);
        for (Worker worker : collection) {
            s = enumMap.get(worker.getPosition());
            s++;
            enumMap.put(worker.getPosition(), s);
        }
        for (Map.Entry<Position, Integer> entry : enumMap.entrySet()) {
            result += entry.getKey() + " - " + entry.getValue();
        }
        return result;
    }

    public void removeById(Long id){
        collection.removeIf(worker -> worker.getId() == id);
    }

    public boolean removeGreater(Worker worker) {
        boolean temp = false;
        for (Worker worker1 : collection) {
            if (worker.compareTo(worker1) > 0) {
                collection.remove(worker1);
                temp = true;
            }
        }
        return temp;
    }

    public boolean removeLower(Worker worker) {
        boolean temp = false;
        for (Worker worker1 : collection) {
            if (worker.compareTo(worker1) > 0) {
                collection.remove(worker1);
                temp = true;
            }
        }
        return temp;
    }

    public LinkedList<Worker> show() {
        return collection.stream().sorted(Comparator.comparing(Worker::getName)).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
    }

    public void update (Long id, Worker worker){
        collection.forEach(worker1 -> {
            if (worker1.getId() == id) {
                worker1.setName(worker.getName());
                worker1.setCoordinates(worker.getCoordinates());
                worker1.setSalary(worker.getSalary());
                worker1.setStartDate(worker.getStartDate());
                worker1.setEndDate(worker.getEndDate());
                worker1.setPosition(worker.getPosition());
                worker1.setPerson(worker.getPerson());
            }
        });
    }

    /**
     * @param id id of required worker
     * @return worker with required id
     */
    public Worker getById(long id) {
        for (Worker worker : collection) {
            if (worker.getId() == id) {
                return worker;
            }
        }
        return null;
    }

    /**
     * @param worker worker class instance to be removed
     */
    public void remove(Worker worker) {
        ExeDone = true;
        collection.remove(worker);
    }

    /**
     * Remove all elements from collection
     */
    public void clear() {
        ExeDone = true;
        collection.clear();
    }

    /**
     * @return true if collection have unsaved changes
     */
    public boolean exeDone() {
        return ExeDone;
    }

    /**
     * @return string array with information about collection
     */
    public String[] getInfo() {
        String Type = "Type: Collection of worker's type objects";
        String Init = "Initialization time: " + InitTime.toString();
        String Size = "Number of elements: " + collection.size();
        String State;
        if (exeDone()) {
            State = "Collection has been modified.";
        } else {
            State = "Collection hasn't been modified yet.";
        }
        return new String[]{Type, Init, Size, State};
    }

    /**
     * @return copy collection with workers
     */
    public LinkedList<Worker> getCollection() {
        return new LinkedList<>(collection);
    }
    public void setCollection(LinkedList col){collection = (LinkedList<Worker>) col;}

    /**
     * Load collection from indicated file
     * @param collectionFromFile external collection of worker instances
     */
    public void load(Collection<Worker> collectionFromFile) {
        collection.addAll(collectionFromFile);
        ExeDone = true;
    }



}