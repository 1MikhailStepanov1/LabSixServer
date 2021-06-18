package command;

import utility.Receiver;

public class AddIfMax extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.addIfMax();
    }
}
