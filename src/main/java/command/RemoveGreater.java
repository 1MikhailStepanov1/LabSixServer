package command;

import utility.Receiver;

public class RemoveGreater extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.removeGreater();
    }
}
