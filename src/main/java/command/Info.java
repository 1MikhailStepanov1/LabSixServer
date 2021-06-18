package command;

import utility.Receiver;

public class Info extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.info();
    }
}
