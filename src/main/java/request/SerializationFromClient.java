package request;

import data.Worker;

import java.io.Serializable;

public class SerializationFromClient implements Serializable {
    private String command;
    private String arg;
    private Worker worker;

    public SerializationFromClient(String command, String arg, Worker worker) {
        this.command = command;
        this.arg = arg;
        this.worker = worker;
    }

    public String getCommand() {
        return command;
    }

    public String getArg() {
        return arg;
    }

    public Worker getWorker() {
        return worker;
    }


}
