package request;

import data.Worker;

import java.io.Serializable;
import java.util.LinkedList;

public class SerializationForClient implements Serializable {
    private boolean status;
    private String message;
    private Integer count;
    private LinkedList<Worker> workers;

    public SerializationForClient(boolean status, String message, Integer count, LinkedList<Worker> workers) {
        this.status = status;
        this.message = message;
        this.count = count;
        this.workers = workers;
    }

    public boolean getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }

    public Integer getCount() {
        return count;
    }
    public LinkedList<Worker> getWorkers(){
        return workers;
    }

    public void setWorkers(LinkedList<Worker> workers) {
        this.workers = workers;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
