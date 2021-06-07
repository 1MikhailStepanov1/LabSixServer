package server;

import data.Worker;

import java.io.Serializable;

public class SerializeData implements Serializable {
    private final Object data;

    public SerializeData(String line){
        data = line;
    }

    public SerializeData(Worker worker){
        data = worker;
    }

    public Object getData() {
        return data;
    }

    public Worker getWorker(){
        if (data instanceof Worker){
            return ((Worker) data);
        }
        return null;
    }
}
