package command;

public class CommandInvoker {
    private final CommandReceiver commandReceiver;


    public CommandInvoker(CommandReceiver commandReceiver){
        this.commandReceiver = commandReceiver;
    }

    public void execute(){

    }
}
