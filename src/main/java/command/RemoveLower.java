package command;

import utility.Receiver;

public class RemoveLower extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.removeLower();
    }
}