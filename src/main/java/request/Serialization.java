package request;

import data.Worker;

import java.io.Serializable;

public class Serialization implements Serializable {
    private String command;
    private String arg;
    private Worker worker;
    private Object data;

    public Serialization(String command, String arg, Worker worker){
        this.command = command;
        this.arg = arg;
        this.worker = worker;
    }

    public Serialization(String line){
        data = line;
    }
    public Serialization(Worker worker){
        data = worker;
    }

    public Object getData(){
        return data;
    }

    public String getCommand(){
        return command;
    }

    public String getArg(){
        return arg;
    }

    public Worker getWorker(){
        return worker;
    }
}
