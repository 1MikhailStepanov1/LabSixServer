package command;

import utility.Receiver;

public class Show extends CommandAbstract{
    @Override
    public void exe(String arg){
        Receiver receiver = new Receiver();
        receiver.show();
    }
}
