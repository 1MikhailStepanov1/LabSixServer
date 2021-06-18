package command;

import utility.Receiver;

public class RemoveById extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.removeById(arg);
    }
}
