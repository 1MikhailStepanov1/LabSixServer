package request;

import data.Worker;

import java.io.Serializable;

public class Serialization implements Serializable {
    private String command;
    private String arg;
    private Worker worker;
    private String dataLine;
    private Worker dataWorker;

    public Serialization(String command, String arg, Worker worker){
        this.command = command;
        this.arg = arg;
        this.worker = worker;
    }

    public Serialization(String line){
        dataLine = line;
    }
    public Serialization(Worker worker){
        dataWorker = worker;
    }

    public String getDataLine(){
        return dataLine;
    }
    public Worker getDataWorker(){
        return dataWorker;
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
