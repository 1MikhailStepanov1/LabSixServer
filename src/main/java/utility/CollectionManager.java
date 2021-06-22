package utility;

import data.Position;
import data.Worker;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;


/**
 * This class is used to do all operations with collection
 */
@XmlRootElement(name = "workers")
public class CollectionManager {
    private LinkedList<Worker> collection = new LinkedList<>();
    private boolean ExeDone;
    private String path;
    private final ZonedDateTime InitTime = ZonedDateTime.now();

    public CollectionManager() {
    }

    /**
     * Adds new worker to the collection
     *
     * @param worker worker instance to be add
     */
    public void add(Worker worker) {
        ExeDone = true;
        collection.add(worker);
    }

    public void addIfMax(Worker worker) {
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

    public Integer countLessThanStartDate(String arg) {
        ZonedDateTime tempTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
        try {
            tempTime = ZonedDateTime.parse(arg, formatter);
        } catch (DateTimeParseException exception) {
            System.out.println(exception.getMessage());
        }
        ArrayList<Worker> tempCollection = null;
        Integer result;
        if (collection.size() > 0) {
            ZonedDateTime finalTempTime = tempTime;
            tempCollection = collection.stream().filter((worker) -> worker.getStartDate().compareTo(finalTempTime) < 0).collect(ArrayList::new, ArrayList::add, ArrayList::addAll);
            result = tempCollection.size();
        } else {
            result = null;
        }
        return result;
    }

    public LinkedList<Worker> filterGreaterThanStartDate(String arg) {
        ZonedDateTime tempTime = null;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.uuuu H:mm:ss z");
        try {
            tempTime = ZonedDateTime.parse(arg, formatter);
        } catch (DateTimeParseException exception) {
            System.out.println(exception.getMessage());
        }
        LinkedList<Worker> result = null;
        if (collection.size() > 0) {
            ZonedDateTime finalTempTime = tempTime;
            result = collection.stream().filter((worker) -> worker.getStartDate().compareTo(finalTempTime) > 0).collect(LinkedList::new, LinkedList::add, LinkedList::addAll);
            return result;
        }
        return result;
    }

    public String groupCountingByPosition() {
        StringBuilder result = null;
        Map<data.Position, Long> answer = collection.stream().collect(Collectors.groupingBy(Worker::getPosition, Collectors.counting()));
        for (Map.Entry<Position, Long> entry : answer.entrySet()) {
            result.append(entry.getKey().toString()).append(" - ").append(entry.getValue());
        }
        return result.toString();
    }

    public void removeById(Long id) {
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

    public void update(Long id, Worker worker) {
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
    public String getInfo() {
        String Type = "Type: Collection of worker's type objects\n";
        String Init = "Initialization time: " + InitTime.toString() + "\n";
        String Size = "Number of elements: " + collection.size()+ "\n";
        String State;
        if (exeDone()) {
            State = "Collection has been modified.";
        } else {
            State = "Collection hasn't been modified yet.";
        }
        StringBuilder result = null;
        result.append(Type).append(Init).append(Size).append(State);
        return String.valueOf(result);
    }

    /**
     * @return copy collection with workers
     */
    public LinkedList<Worker> getCollection() {
        return collection;
    }

    @XmlElement(name = "worker")
    public void setCollection(LinkedList<Worker> collection) {
        this.collection = collection;
    }

    /**
     * Load collection from indicated file
     *
     * @param collectionFromFile external collection of worker instances
     */
    public void load(Collection<Worker> collectionFromFile) {
        collection.addAll(collectionFromFile);
    }

    public void setPath(String path){
        this.path = path;
    }
    public String getPath(){
        return path;
    }


}