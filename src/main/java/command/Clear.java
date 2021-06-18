package command;

import utility.Receiver;

public class Clear extends CommandAbstract {
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.clear();
    }
}
